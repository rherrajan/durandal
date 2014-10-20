package tk.icudi.durandal.core.view;

import tk.icudi.durandal.core.logic.BoardModel;
import tk.icudi.durandal.core.logic.Point;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.Log;

public class BoardTranslation {
	
	private float scale;
	
	Point origin;
	
	private Rect field_borders;
	private Rect command_borders;

	Matrix matrix;

	BoardTranslation(){
        this.matrix = new Matrix();
	}

	void calculateBounds(int screenHeightMin, int screenHeightMax, int screenWidth) {
		
		int fieldHeight = screenHeightMax - screenHeightMin;
		int fieldWidth = (int)(screenWidth * 0.9); // Noch etwas Platz für Buttons lassen
		
		// Wir berechnen die Größe des Spielfeldes so, dass es in den Screen hineinpasst

		float scaleW = fieldWidth / (float) BoardModel.width;
		float scaleH = fieldHeight / (float) BoardModel.height;
		float scale = Math.min(scaleW, scaleH);

		field_borders = new Rect(0, 0, (int) (scale * BoardModel.width), (int) (scale * BoardModel.height));

		// Mittig
		int widthDiff = (fieldWidth - field_borders.right) / 2;
		field_borders.left += widthDiff;
		field_borders.right += widthDiff;

		schrink(field_borders, 5);

		scaleW = (field_borders.right - field_borders.left) / (float) BoardModel.width;
		scaleH = (field_borders.bottom - field_borders.top) / (float) BoardModel.height;
		scale = Math.min(scaleW, scaleH);
		
		Log.d(" --- ", "board view calculations: Screen=" + screenWidth + "/" + (screenHeightMax - screenHeightMin) + " Board: " + fieldWidth + "/" + fieldHeight + " Field: " +  field_borders + " Scale: " + scale);

		int width = (int)((field_borders.right - field_borders.left)*0.1);
		this.command_borders = new Rect(field_borders.right, field_borders.top, field_borders.right + width, field_borders.bottom);
		this.scale = scale;

	}
	
	private void schrink(Rect rect, int amount) {
		rect.left += amount;
		rect.top += amount;
		rect.right -= amount;
		rect.bottom -= amount;
	}

	protected Rect transformFieldToView(Rect source) {
		Rect result = new Rect();
		result.left = (int) (field_borders.left + this.scale * source.left);
		result.right = (int) (field_borders.left + this.scale * source.right);
		result.top = (int) (field_borders.top + this.scale * source.top);
		result.bottom = (int) (field_borders.top + this.scale * source.bottom);
		return result;
	}

	protected Point transformFieldToView(Point source) {
		Point result = new Point();
		result.x = (int) (field_borders.left + this.scale * source.x);
		result.y = (int) (field_borders.top + this.scale * source.y);
		return result;
	}
	
	protected Point transformViewToField(float source_x, float source_y) {
		Point result = new Point();
		result.x = (int) ((source_x - field_borders.left) / this.scale);
		result.y = (int) ((source_y - field_borders.top) / this.scale);
		return result;
	}

    float[] transformScreenToView(float[] orgTapEvent){

		float[] tapEvent = new float[orgTapEvent.length];
		for(int i=0; i<orgTapEvent.length; ++i){
			tapEvent[i] = orgTapEvent[i];
		}
		
    	Matrix inverse = new Matrix();
		boolean inverseable = matrix.invert(inverse);

		if(inverseable){
			inverse.mapPoints(tapEvent);
		} else {
	        System.out.println(" --- matrix is not inverseable: " + inverseable);
		}

		return tapEvent;

    }
    
    enum Region {
    	field,
    	button,
    	nothing
    }
    
	protected Region regionOfTab(float[] tapEvent) {
		        
		if(field_borders.contains((int)tapEvent[0], (int)tapEvent[1])){
			return Region.field;
		} else if(command_borders.contains((int)tapEvent[0], (int)tapEvent[1])) {
			return Region.button;
		} else {
			return Region.nothing;
		}

	}

	protected Rect getField_borders() {
		return field_borders;
	}

	protected Rect getCommand_borders() {
		return command_borders;
	}

	protected float getScale() {
		return scale;
	}


}
