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
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import br.edu.ufcg.BD.BDAdapter;
import br.edu.ufcg.model.Categoria;
import br.edu.ufcg.model.Roupa;

public class TirarFotoRoupaActivity extends Activity implements ImageListener {

	private BDAdapter dao;
	
	private CameraView mPreview;

	private LinearLayout layout;
	private int[] moldes;
	private Categoria[] categorias;
	
	private int indice = 0;

	private ImageButton proximoMoldeButton;

	private ImageView voltaMoldeButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_roupas);
		mPreview = (CameraView) findViewById(R.id.imagem_camera_roupas);
		mPreview.addImageListener(this);
		
		this.dao = new BDAdapter(this);

		//FIXME o segundo molde de short deveria ser um molde espec�fico pra bermuda, j� que h� diferen�a entre eles.
		moldes = new int[] {R.drawable.molde_short, R.drawable.molde_calca,
				R.drawable.molde_camisa, R.drawable.molde_camisao, R.drawable.molde_camiseta,
				R.drawable.molde_saia, R.drawable.molde_short, R.drawable.molde_camiseta};
		categorias = Categoria.values();
		
		if (moldes.length != categorias.length) Log.e("ERRO NOSSO", "O NUMERO DE MOLDES TEM QUE SER IGUAL AO NUMERO DE CATEGORIAS! JAH PODE OLHAR NO ONCREATE DE TIRARFOTOROUPAACTIVITY");
		
        layout = new LinearLayout(this);
        layout.setBackgroundResource(moldes[indice]);
        
        addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        
        
        proximoMoldeButton = new ImageButton(this);
        proximoMoldeButton.setImageResource(R.drawable.next);
		proximoMoldeButton.setBackgroundColor(Color.TRANSPARENT);
		proximoMoldeButton.setOnClickListener(new ProximoListener());

		voltaMoldeButton = new ImageButton(this);
		voltaMoldeButton.setImageResource(R.drawable.previous_cinza);
		voltaMoldeButton.setBackgroundColor(Color.TRANSPARENT);
		voltaMoldeButton.setOnClickListener(new VoltaListener());

		RelativeLayout layout = new RelativeLayout(this);
		LinearLayout linear = new LinearLayout(this);
		linear.addView(voltaMoldeButton);
		linear.addView(proximoMoldeButton);
		linear.setGravity(Gravity.RIGHT);
		linear.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		layout.addView(linear);
		layout.setGravity(Gravity.BOTTOM);

		addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	private class VoltaListener implements OnClickListener {

		public void onClick(View arg0) {
			if (indice != 0) {
				indice--;
				layout.setBackgroundResource(moldes[indice]);
			} 
			atualizaImagensBotoes();
		}
		
	}

	private class ProximoListener implements OnClickListener {

		public void onClick(View arg0) {
			if (indice != categorias.length - 1) {
				indice++;
				layout.setBackgroundResource(moldes[indice]);
			}
			atualizaImagensBotoes();
		}
	}
	
	private void atualizaImagensBotoes() {
		if (indice > 0) {
			voltaMoldeButton.setImageResource(R.drawable.previous);
		} else {
			voltaMoldeButton.setImageResource(R.drawable.previous_cinza);
		}

		if (indice < moldes.length - 1) {
			proximoMoldeButton.setImageResource(R.drawable.next);
		} else {
			proximoMoldeButton.setImageResource(R.drawable.next_cinza);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflate = new MenuInflater(this);
		inflate.inflate(R.menu.tirar_foto_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		mPreview.takePicture();
		return true;
	}

	public void takeImage(byte[] image) { 
		// Cleaning memory 
		System.gc(); 
		mPreview.setVisibility(View.INVISIBLE);
		mPreview.freezeCamera();

		Bitmap plecas = BitmapFactory.decodeByteArray(image, 0, image.length);
		Bitmap mudado = Bitmap.createScaledBitmap(plecas, 400, 400, true);
		mudado = JPEGtoRGB888(mudado); 
		
		Bitmap roupaSemBackground = tiraBackground(mudado);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
		roupaSemBackground.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos); 
		byte[] bitmapdata = bos.toByteArray();
		
		Roupa roupa = new Roupa(0, bitmapdata, categorias[indice]);
		this.dao.inserirRoupa(roupa);

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