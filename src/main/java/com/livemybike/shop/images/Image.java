package com.livemybike.shop.images;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Immutable image object
 */
public class Image {

    private String url;
    private String name;
    private String contentType;
    private long size;
    private byte[] bytes;
    private ImageSize imageSize;

    public Image(InputStream inputStream, String name, long size, String contentType) {
        this(inputStream, name, size, contentType, ImageSize.ORIGINAL);
    }

    private Image(InputStream inputStream, String name, long size, String contentType, ImageSize imageSize) {
        try {
            this.bytes = getBytes(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.imageSize = imageSize;
    }

    public InputStream getImageInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getContentType() {
        return contentType;
    }

    public ImageSize getImageSize() {
        return imageSize;
    }

    public Image resizeToSmall() throws IOException {
        return resizeImage(ImageSize.SMALL);
    }

    public Image resizeToMedium() throws IOException {
        return resizeImage(ImageSize.MEDIUM);
    }

    /**
     * Create a new resized image with the desired height
     * 
     * @param imageSize
     *            see {@link ImageSize}
     * @return new image
     * @throws IOException
     *             if there is a problem with reading the image stream
     */
    private Image resizeImage(ImageSize imageSize) throws IOException {
        BufferedImage originalImage = ImageIO.read(getImageInputStream());
        int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        double ratio = (double) originalImage.getWidth() / originalImage.getHeight();
        int imageWidth = (int) (imageSize.getHeight() * ratio);

        BufferedImage resizedImage = new BufferedImage(imageWidth, imageSize.getHeight(), type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, imageWidth, imageSize.getHeight(), null);
        g.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // TODO: could have a problem if the image file contains more than 1 '.'
        ImageIO.write(originalImage, name.split("\\.")[1], outputStream);

        return new Image(new ByteArrayInputStream(outputStream.toByteArray()),
                name, outputStream.size(), contentType, imageSize);
    }

    private byte[] getBytes(InputStream in) throws IOException {
        byte[] buff = new byte[8000];
        int bytesRead = 0;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        while ((bytesRead = in.read(buff)) != -1) {
            bao.write(buff, 0, bytesRead);
        }

        return bao.toByteArray();
    }

}
