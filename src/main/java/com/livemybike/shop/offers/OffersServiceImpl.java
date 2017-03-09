package com.livemybike.shop.offers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.livemybike.shop.security.Account;
import com.livemybike.shop.security.AccountRepo;
import com.livemybike.shop.security.AnonymousAuthNotAllowedException;

@Service
public class OffersServiceImpl implements OffersService {

    private static final String S3_ACESS_KEY = "AKIAJXV23NQFIODDAROQ";
    private static final String S3_SECRET_KEY = "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";
    private static final String S3_BUCKET = "bike-bucket";
    private static final String S3_URI = "https://s3.eu-central-1.amazonaws.com/bike-bucket/";
    private static final String IMAGE_SIZE_PREFIX_SMALL = "s";
    private static final String IMAGE_SIZE_PREFIX_MEDIUM = "m";
    private static final int IMG_HEIGHT_SMALL = 200;
    private static final int IMG_HEIGHT_MEDIUM = 800;

    private static final int PAGE_SIZE = 12;

    @Autowired
    private OffersRepo offersRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<OfferDto> listOffers(String genderFilter, String location, int pageNumber) {
        // TODO: find a way to build a dynamic criteria
        if (StringUtils.isEmpty(genderFilter) && StringUtils.isEmpty(location)) {
            // Not filter passed. Return everything.
            return convertToDtoList(offersRepo.findAll(getPage(pageNumber)));
        } else if (!StringUtils.isEmpty(genderFilter) && StringUtils.isEmpty(location)) {
            // only gender filter passed
            List<String> filters = getGenderFiltersList(genderFilter);
            return convertToDtoList(offersRepo.findByGenderIn(filters, getPage(pageNumber)));
        } else if (StringUtils.isEmpty(genderFilter) && !StringUtils.isEmpty(location)) {
            // only location filter passed
            return convertToDtoList(offersRepo.findByCityIgnoreCaseOrPostcodeIgnoreCase(location, location, getPage(pageNumber)));
        } else {
            // location and gender filter passed
            List<String> filters = getGenderFiltersList(genderFilter);
            return convertToDtoList(offersRepo.findByGenderAndLocation(filters, location, getPage(pageNumber)));
        }
    }

    private List<String> getGenderFiltersList(String genderFilter) {
        return genderFilter.chars()
                        .mapToObj(c -> String.valueOf((char) c))
                        .collect(Collectors.toList());
    }

    @Override
    public OfferDto createOffer(String title, String price, String gender, String description, String street,
                                String number, String postcode, String city, MultipartFile image0, MultipartFile image1,
                                MultipartFile image2, MultipartFile image3, MultipartFile image4, MultipartFile image5) {
        Offer offer = new Offer();

        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if (user instanceof AnonymousAuthenticationToken) {
            throw new AnonymousAuthNotAllowedException(
                    "User should be logged in before posting an offer");
        }

        User authIdentity = (User) user.getPrincipal();
        Account loggedInAccount = accountRepo.findByEmail(authIdentity.getUsername());

        offer.setOwner(loggedInAccount);
        offer.setTitle(title);
        offer.setPrice(new BigDecimal(price));
        offer.setGender(gender);
        offer.setDescription(description);
        offer.setStreet(street);
        offer.setNumber(number);
        offer.setPostcode(postcode);
        offer.setCity(city);

        offer.setImage0(image0.getOriginalFilename());

        if (image1 != null) { offer.setImage1(image1.getOriginalFilename()); }
        if (image2 != null) { offer.setImage2(image2.getOriginalFilename()); }
        if (image3 != null) { offer.setImage3(image3.getOriginalFilename()); }
        if (image4 != null) { offer.setImage4(image4.getOriginalFilename()); }
        if (image5 != null) { offer.setImage5(image5.getOriginalFilename()); }

        Offer result = offersRepo.save(offer);

        storeImages(result.getId(), image0, image1, image2, image3, image4, image5);

        return convertToDto(result);
    }

    @Override
    public Page<OfferDto> listMyOffers(int pageNumber) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        if (user instanceof AnonymousAuthenticationToken) {
            throw new AnonymousAuthNotAllowedException(
                    "User should be logged in before to post an offer");
        }

        User authIdentity = (User) user.getPrincipal();
        Account loggedInAccount = accountRepo.findByEmail(authIdentity.getUsername());
        Page<Offer> offers = offersRepo.findByOwner(loggedInAccount, getPage(pageNumber));
        return convertToDtoList(offers);
    }

    private OfferDto convertToDto(Offer offer) {
        OfferDto result = modelMapper.map(offer, OfferDto.class);
        result.setOwner_id(offer.getOwner().getId());
        return result;
    }

    private void storeImages(Long offerId, MultipartFile image0, MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4, MultipartFile image5) {
        resizeAndStoreToS3(offerId, image0);

        if (image1 != null) { resizeAndStoreToS3(offerId, image1); }
        if (image2 != null) { resizeAndStoreToS3(offerId, image2); }
        if (image3 != null) { resizeAndStoreToS3(offerId, image3); }
        if (image4 != null) { resizeAndStoreToS3(offerId, image4); }
        if (image5 != null) { resizeAndStoreToS3(offerId, image5); }
    }

    private void resizeAndStoreToS3(Long offerId, MultipartFile image) {
        BufferedImage resizedImageSmall = resizeImage(image, IMG_HEIGHT_SMALL);
        BufferedImage resizedImageMedium = resizeImage(image, IMG_HEIGHT_MEDIUM);

        AmazonS3 amazonS3;
        AWSCredentials awsCredentials = new BasicAWSCredentials(S3_ACESS_KEY, S3_SECRET_KEY);
        amazonS3 = new AmazonS3Client(awsCredentials);
        Region euFrankfurt = Region.getRegion(Regions.EU_CENTRAL_1);
        amazonS3.setRegion(euFrankfurt);

        storeToS3(offerId, image, resizedImageSmall, amazonS3, IMAGE_SIZE_PREFIX_SMALL);
        storeToS3(offerId, image, resizedImageMedium, amazonS3, IMAGE_SIZE_PREFIX_MEDIUM);
    }

    private BufferedImage resizeImage(MultipartFile image, int IMG_HEIGHT_S) {
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(image.getInputStream());
        } catch (IOException e) {
            throw new ImageStoringException(
                    "Unable to store image: " + image.getOriginalFilename());
        }
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        double ratio = (double)originalImage.getWidth() / originalImage.getHeight();
        int imageWidth = (int)(IMG_HEIGHT_S * ratio);

        BufferedImage resizedImage = new BufferedImage(imageWidth, IMG_HEIGHT_S, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, imageWidth, IMG_HEIGHT_S, null);
        g.dispose();
        return resizedImage;
    }

    private void storeToS3(Long offerId, MultipartFile image, BufferedImage resizedImageSmall, AmazonS3 amazonS3, String sizePrefix) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImageSmall, image.getOriginalFilename().split("\\.")[1], outputStream);
        } catch (IOException e) {
            throw new ImageStoringException(
                    "Unable to store image: " + image.getOriginalFilename());
        }
        byte[] buffer = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(buffer);

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(image.getContentType());
        meta.setContentLength(buffer.length);

        amazonS3.putObject(S3_BUCKET, offerId + "_" + sizePrefix + "_" + image.getOriginalFilename(), inputStream, meta);
    }

    private PageRequest getPage(int pageNumber) {
        // TODO: think about cache
        return new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "id");
    }

    private Page<OfferDto> convertToDtoList(Page<Offer> offerPage) {
        return offerPage.map(offer -> convertToDto(offer));
    }

}
