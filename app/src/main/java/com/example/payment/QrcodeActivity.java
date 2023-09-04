package com.example.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

public class QrcodeActivity extends AppCompatActivity {

    private ImageView qrCodeImageView;
    TextView unumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        Intent intent = getIntent();
        String number = intent.getStringExtra("number");
        Bitmap qrCodeBitmap = generateQRCode(number);
        qrCodeImageView.setImageBitmap(qrCodeBitmap);
        unumber  =  findViewById(R.id.number);
        unumber.setText(number);
    }
    private Bitmap generateQRCode(String content) {
        try {
            // Set up encoding hints if needed (optional)
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Generate the QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 512, 512, hints);

            // Convert BitMatrix to Bitmap
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? getResources().getColor(R.color.black) : getResources().getColor(R.color.white));
                }
            }

            return qrCodeBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}