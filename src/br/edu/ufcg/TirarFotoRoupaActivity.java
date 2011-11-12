package br.edu.ufcg;

import java.io.ByteArrayOutputStream;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Roupa;

public class TirarFotoRoupaActivity extends Activity implements ImageListener, OnClickListener {

	private CameraView mPreview;
	private BDAdapter dh;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_roupas);
		mPreview = (CameraView) findViewById(R.id.imagem_camera_roupas);
		mPreview.addImageListener(this);
		
		Bundle params = getIntent().getExtras();
        Categoria categoria = (Categoria) params.get("categoria");

        LinearLayout layout = new LinearLayout(this);
        
        //codigo feio pra funfar, ajeitar depois =P
        if (categoria.toString()=="Bermuda") layout.setBackgroundResource(R.drawable.molde_short);
        else if (categoria.toString()=="Calça") layout.setBackgroundResource(R.drawable.molde_calca);
        else if (categoria.toString()=="Camisa") layout.setBackgroundResource(R.drawable.molde_camisa);
        else if (categoria.toString()=="Camisa manga longa") layout.setBackgroundResource(R.drawable.molde_camisao);
        else if (categoria.toString()=="Camiseta") layout.setBackgroundResource(R.drawable.molde_camiseta);
        else if (categoria.toString()=="Saia") layout.setBackgroundResource(R.drawable.molde_saia);
        else if (categoria.toString()=="Short") layout.setBackgroundResource(R.drawable.molde_short);
        else layout.setBackgroundResource(R.drawable.molde_camiseta);
        
        
        addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		Button tiraFoto = new Button(this);
		tiraFoto.setBackgroundColor(Color.TRANSPARENT);
		tiraFoto.setOnClickListener(this);
		addContentView(tiraFoto, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		this.dh = new BDAdapter(this);

	}

	public void takeImage(byte[] image) { 
		// Cleaning memory 
		System.gc(); 
		mPreview.setVisibility(View.INVISIBLE);
		mPreview.freezeCamera();

		Bundle params = getIntent().getExtras();
		Categoria categoria = (Categoria) params.get("categoria");
		
		
		Bitmap plecas = BitmapFactory.decodeByteArray(image, 0, image.length);
		Bitmap mudado = Bitmap.createScaledBitmap(plecas, 400, 400, true);
		mudado = JPEGtoRGB888(mudado); 
		
		Bitmap roupaSemBackground = tiraBackground(mudado);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		roupaSemBackground.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		
		Roupa roupa = new Roupa(0, bitmapdata, categoria);
		this.dh.inserirRoupa(roupa);

		this.finish();
	}
	
	 /** Takes a JPEG captured by the device camera and converts it to 
	RGB888 format */ 
	    private Bitmap JPEGtoRGB888(Bitmap img) 
	    { 
	        int numPixels = img.getWidth()* img.getHeight(); 
	        int[] pixels = new int[numPixels]; 
	        //Get JPEG pixels.  Each int is the color values for one pixel. 
	        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), 
	img.getHeight()); 
	        //Create a Bitmap of the appropriate format. 
	        Bitmap result = Bitmap.createBitmap(img.getWidth(), 
	img.getHeight(), Config.ARGB_8888); 
	        //Set RGB pixels. 
	        result.setPixels(pixels, 0, result.getWidth(), 0, 0, 
	result.getWidth(), result.getHeight()); 
	        return result; 
	    } 

	private Bitmap tiraBackground(Bitmap mudado) {
		//Lendo uma imagem e transformando em matriz
		
		Mat orig = Utils.bitmapToMat(mudado);
		
		//gerando uma imagem com canais de cores compativeis com as transformacoes
//		Mat mRgba2 = orig.clone();
//		Imgproc.cvtColor(orig, mRgba2, Imgproc.COLOR_RGB2BGRA, 4);
		
		Bitmap bmpOriginal = Bitmap.createBitmap(orig.cols(), orig.rows(), Bitmap.Config.ARGB_8888);
        
		Mat mIntermediateMat = orig.clone();
//		Mat mRgba = orig.clone();
		
		//Realizando o threshold com limiar = 128
		Imgproc.threshold(orig, mIntermediateMat, 128, 255, Imgproc.THRESH_BINARY);
//    	Imgproc.cvtColor(mIntermediateMat, mRgba, Imgproc.COLOR_RGB2BGRA);
		
		Bitmap bmp = Bitmap.createBitmap(orig.cols(), orig.rows(),
				Bitmap.Config.ARGB_8888);
		
		//transformando as matrizes para bitmaps
		
		 Utils.matToBitmap(orig, bmpOriginal);
         Utils.matToBitmap(mIntermediateMat, bmp);
		
//		Utils.matToBitmap(mRgba2, bmpOriginal);
//		Utils.matToBitmap(mRgba, bmp);
		
//		return bmpOriginal;
		return posProcessamento(bmpOriginal, bmp);
	}

	public void onClick(View v) {
		mPreview.takePicture();

	}
	
	
	 /**
     * Realiza o pos-processamento na imagem limiarizada. Os pixels da imagem
     * que sao brancos, permanecem assim enquanto os pixels pretos sao substituidos
     * pelo pixel equivalente na imagem original.
     * 
     * @param orig
     * @param lim
     * @return um novo bitmap
     */
    private Bitmap posProcessamento(Bitmap orig, Bitmap lim) {
		int width = orig.getWidth();
		int height = orig.getHeight();
		
		if (height != lim.getHeight() || width != lim.getWidth())
			throw new IllegalArgumentException("As imagens devem possuir o mesmo tamanho");
		
		Bitmap result = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		
		int pixelOrig;
		int pixelLimiarizado;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				pixelOrig = orig.getPixel(x, y);
				pixelLimiarizado = lim.getPixel(x, y);
//				System.err.println(">>>>>> pixel: " + pixelLimiarizado);
				if (pixelLimiarizado == Color.WHITE) {
					result.setPixel(x, y, Color.TRANSPARENT);
				} else {
					result.setPixel(x, y, pixelOrig);
				}
			}
		}
		return result;
	}


}
