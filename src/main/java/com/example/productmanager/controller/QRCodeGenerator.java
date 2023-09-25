package com.example.productmanager.controller;



import net.glxn.qrgen.javase.QRCode;
import net.glxn.qrgen.core.image.ImageType;
import java.io.ByteArrayOutputStream;

public class QRCodeGenerator {
    public static ByteArrayOutputStream generateQRCodeImage(String qrCodeText, int width, int height) {
        return QRCode.from(qrCodeText).to(ImageType.PNG).withSize(width, height).stream();
    }
}
