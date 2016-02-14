package tk.icudi.durandal.core.view;

import java.util.ArrayList;
import java.util.List;

import tk.icudi.durandal.core.logic.GameLogic;
import tk.icudi.durandal.core.logic.Point;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameView extends android.view.View {
	
	private GameLogic logic;
		
	private List<BoardView> boardViews = new ArrayList<BoardView>();
	
	boolean init = false;

	public GameView(Context context) {
		super(context);
		this.setBackgroundColor(Color.BLACK);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	@Override
	protected void onDraw(Canvas c) {
		
		if(logic.getTick() == -1){
			return;
		}
		
		super.onDraw(c);
		
		if(init == false){
			initBoards();
			init = true;
		}
		
		for(BoardView boardView : boardViews){
			c.save();
			
			c.setMatrix(boardView.matrix);
			boardView.drawBoard(c);
			
			c.restore();
		}		

	}

	private void initBoards() {
		
		int boardCount = logic.getBoards().size();
		
		for(int i=0; i<boardCount; i++){
			BoardView boardView = createBoardView(i, boardCount);
			this.boardViews.add(boardView);
		}

	}

	private BoardView createBoardView(int playerNr, int maxPlayer) {
		
		BoardView board_view = new BoardView();
		board_view.setOrigin(new Point(getWidth()/2, getHeight()/2));
		board_view.setBoard(logic.getBoards().get(playerNr));

		
		final int minHeight = (int)(0.5f * this.getHeight());
		
		if(playerNr == 0){
			board_view.matrix.setTranslate(0.0f, 0.5f * this.getHeight() );
			
		} else {
			
			if(maxPlayer > 2){
				board_view.matrix.setScale(0.5f, 0.5f);
			}
			
			if(playerNr % 2 == 0){
				board_view.matrix.postTranslate(0.5f * this.getWidth(), 0.0f);
			}
			if(playerNr > maxPlayer/2){
				board_view.matrix.postTranslate(0.0f, 0.25f * this.getHeight());
			}
			
			board_view.matrix.postTranslate(0.0f, 0.5f * this.getHeight());
			board_view.matrix.postRotate(180, board_view.origin.x, board_view.origin.y);
		}
			
		board_view.calculateBounds(minHeight, this.getHeight(), this.getWidth());
		return board_view;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        if(this.logic.isPaused() == false && this.logic.isFinished() == false){
	        	this.logic.setPaused(true);
	        	Log.d(" --- ", "Game paused");
	        	return true;
	        }
	    } else if(keyCode == KeyEvent.KEYCODE_MENU){
	    	Log.d(" --- ", "Button: KEYCODE_MENU");
	    } else {
			Log.d(" --- ", "Button: " + keyCode);
	    }
	    
	    return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if(logic.isPaused()){
			logic.setPaused(false);
		}
		
		if (logic.getWaitTimeout() == 0) {
			boardViews.get(0).onAfterGameTap();
			return true;
		}
		
		float[] tapEvent = new float[2];
		
		switch(event.getActionMasked()){
			case MotionEvent.ACTION_MOVE:
				// We only count taps. But are still interested in other actions during that move
				return true;
			case MotionEvent.ACTION_POINTER_UP: 
			case MotionEvent.ACTION_UP:
				// Not interested anymore
				return false;
		    case MotionEvent.ACTION_POINTER_DOWN: 
		    case MotionEvent.ACTION_DOWN:
		        final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)  >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	            tapEvent[0] = event.getX(pointerIndex);
	            tapEvent[1]  = event.getY(pointerIndex);
		        break;

			default:
				Log.d(" --- ", "unknown Action: " + event.getAction());

		}	
		
		
		for(BoardView view : boardViews){
			boolean isConsumed = view.onTab(tapEvent);
			if(isConsumed){
				break;
			}
		}
			
		
		return true;
	}

	public GameLogic getLogic() {
		return logic;
	}

	public void setLogic(GameLogic logic) {
		this.logic = logic;
	}

}
