package tk.icudi.durandal.core.view;

import tk.icudi.durandal.core.logic.BoardController;
import tk.icudi.durandal.core.logic.BoardModel;
import tk.icudi.durandal.core.logic.BoardModel.GameResult;
import tk.icudi.durandal.core.logic.Monster;
import tk.icudi.durandal.core.logic.Point;
import tk.icudi.durandal.core.logic.Tower;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseIntArray;

public class BoardView extends BoardTranslation {
	
	private static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private static Paint textPaint;
	
	static SparseIntArray levelcolors = new SparseIntArray();

	static {
		textPaint = new Paint();
		textPaint.setARGB(150, 255, 255, 255);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(100);

		initLevelcolors();		
	}

	private static void initLevelcolors() {
		for(int lvl=0; lvl< 100 ; lvl++){
			int color = colorForLevel(lvl);
			levelcolors.put(lvl, color);
		}
	}
	
	protected BoardModel model;
	private BoardController controller;
	
	void drawBoard(Canvas c) {

		final int backgroundcolor;
		if(controller.isRemoteControlled()){
			backgroundcolor = 0xff334455;
		} else {
			backgroundcolor = 0xff005599;
		}
		// Hintergrund
		paint.setColor(backgroundcolor);
		paint.setStyle(Paint.Style.FILL);
		c.drawRect(getField_borders(), paint);

		// Commands
		paint.setColor(Color.rgb(50, 0, 0));
		paint.setStyle(Paint.Style.FILL);
		c.drawRect(getCommand_borders(), paint);
		
		// Rahmen
		paint.setColor(0xffffffaa);
		paint.setStyle(Paint.Style.STROKE);
		c.drawRect(getField_borders(), paint);

		// Name

		Point textLocation = getTextLocation();
		textPaint.setTextSize(30);
		c.drawText(model.getName(), textLocation.x, textLocation.y, textPaint);
		textPaint.setTextSize(100);
		
		// Monster
		for(int i=0; i<model.getMonsters().size(); i++){
			Monster monster = model.getMonsters().valueAt(i);
				
			Point place = transformFieldToView(monster.getPlace());
			paint.setColor(colorForLevel(monster.getLevel()));
						
			if(monster.getLifepoints() >= 0){
				paint.setStyle(Paint.Style.STROKE);
				int mosterSize = (int) Math.ceil(3*monster.getSize()) + 3;
				c.drawCircle(place.x, place.y, mosterSize, paint);
			} else {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.BLACK);
				paint.setAlpha(50);
				c.drawCircle(place.x, place.y, 3, paint);
			}
			
		}
		
		// Türme
		for (Tower tower : model.getTowers()) {
			paint.setColor(colorForLevel(tower.getLvl()));
			paint.setStyle(Paint.Style.FILL);
			Point place = transformFieldToView(tower.place);
			c.drawCircle(place.x, place.y, 3+tower.getLvl()/10, paint);
			
			paint.setColor(0xff3399ff);
			paint.setStyle(Paint.Style.STROKE);
			c.drawCircle(place.x, place.y, (int) (tower.getRange() * getScale()), paint);
		}

		// Schüsse
		paint.setColor(0xffee8844);
		for (Rect shot : model.getShots()) {
			Rect place = super.transformFieldToView(shot);
			c.drawLine(place.left, place.top, place.right, place.bottom, paint);
		}

		// Ende
		if (model.isFinished()) {

			paintKiller(c);

			Point middle = getMiddle();

			if(model.getGameResult() == GameResult.win){
				c.drawText("You Win", middle.x, middle.y, textPaint);
			} else if(model.getGameResult() == GameResult.loose){
				c.drawText("Game Over", middle.x, middle.y, textPaint);
			}
		} else if(controller.isPaused()){
			Point middle = getMiddle();
			c.drawText("paused", middle.x, middle.y, textPaint);
		}
	}

	private Point getTextLocation() {
		
		int width = getField_borders().left + ((getField_borders().right - getField_borders().left));
		int height = getField_borders().top + ((getField_borders().bottom - getField_borders().top));
		int mid_x = (int)(0.5*width);
		int mid_y = (int)(0.05*height);

		Point middle = new Point(mid_x, mid_y);
		return middle;
	}
	
	private Point getMiddle() {
		int mid_x = getField_borders().left + ((getField_borders().right - getField_borders().left) / 2);
		int mid_y = getField_borders().top + ((getField_borders().bottom - getField_borders().top) / 2);

		Point middle = new Point(mid_x, mid_y);
		return middle;
	}

	private void paintKiller(Canvas c) {
		
		Monster monster = model.getKiller();
		
		if(monster == null){
			return;
		}
		Point place = transformFieldToView(monster.getPlace());
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.FILL);
		c.drawCircle(place.x, place.y, (int) Math.ceil(3*monster.getSize()) + 3, paint);
		paint.setColor(colorForLevel(monster.getLevel()));
		paint.setStyle(Paint.Style.STROKE);
		c.drawCircle(place.x, place.y, (int) Math.ceil(3*monster.getSize()) + 3, paint);
		
		Rect deathPointer = new Rect();
		deathPointer.left = BoardModel.width / 2;
		deathPointer.top = BoardModel.height / 2;
		deathPointer.right = monster.getPlace().x;
		deathPointer.bottom = monster.getPlace().y;
		
		deathPointer = transformFieldToView(deathPointer);

		Path path = new Path();
		path.moveTo(deathPointer.left, deathPointer.top);
		path.lineTo(deathPointer.right, deathPointer.bottom);
		paint.setColor(Color.RED);
		paint.setAlpha(100);
		paint.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));
		c.drawPath(path, paint);
		paint.setPathEffect(null);
	}

	private static int colorForLevel(int level) {

		Integer preparedColor = levelcolors.get(level);
		
		if(preparedColor != 0){
			return preparedColor;
		}
		float[] hsv = new float[3];
		hsv[0] = 100 * (level+3);
		hsv[0] %= 360.0f;
		hsv[1] = 1f;
		hsv[2] = 1f;
		
		int col = Color.HSVToColor(hsv);

		levelcolors.put(level, col);
		
		return col;
	}
	

	public void onAfterGameTap() {
		controller.tapForReset();
	}
	
	public boolean onTab(float[] orgTapEvent) {
		
		float[] tapEvent = super.transformScreenToView(orgTapEvent);
		
		Region region = super.regionOfTab(tapEvent);
		
//		Log.d(" --- " , "tapEvent: " + Arrays.toString(orgTapEvent) + " => " + Arrays.toString(tapEvent) + " => " + region);
		
		switch(region){
			case field:
				Point point = super.transformViewToField(tapEvent[0], tapEvent[1]);
				controller.tapForTower(point);
				return true;
			case button:
				controller.tapForCreep();
				return true;
			case nothing:
				return false;
			default:
				Log.d(" --- ", "unknown region: " + region);
				return false;
		}

	}
	
	public void setOrigin(Point point) {
		this.origin = point;
	}

	public void setBoard(BoardController boardController) {
		this.model = boardController.getModel();
		this.controller = boardController;
	}




}
