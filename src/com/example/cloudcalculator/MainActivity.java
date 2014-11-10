package com.example.cloudcalculator;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	/*  ROW 0 -> 1  2   3   + 
	 *  ROW 1 -> 4  5   6   -
	 *  ROW 2 -> 7  8   9   *
	 *  ROW 3 -> 0  .   B   /
	 *  ROW 4 -> C  +/- 00  =
	 * 
	 */
    private static final int NUM_ROWS = 5;
	private static final int NUM_COLS = 4;
	private static final int KEY0 = 0;
	private static final int KEY1 = 1;
	private static final int KEY2 = 2;
	private static final int KEY3 = 3;
	private static final int KEY4 = 4;
	private static final int KEY5 = 5;
	private static final int KEY6 = 6;
	private static final int KEY7 = 7;
	private static final int KEY8 = 8;
	private static final int KEY9 = 9;
	private static final int KEYDOT = 10;
	private static final int KEYBACK = 11;
	private static final int KEYPLUS = 12;
	private static final int KEYSUB = 13;
	private static final int KEYMUL = 14;
	private static final int KEYDIV = 15;
	private static final int KEYEQUAL = 16;
	private static final int KEYCLEAR = 17;
	double leftnum = 0;
	String leftnumstr ="";
	boolean leftdec = false;
	double rightnum = 0;
	String rightnumstr ="";
	boolean rightdec = false;
	double resultnum = 0;
	boolean resultdec = false;
	int state=0;
	int func=-1;
	Button buttons[][] = new Button[NUM_ROWS][NUM_COLS];
	
	Button BUTTON0 = buttons[3][0];
	Button BUTTON1 = buttons[0][0];
	Button BUTTON2 = buttons[0][1];
	Button BUTTON3 = buttons[0][2];
	Button BUTTON4 = buttons[1][0];
	Button BUTTON5 = buttons[1][1];
	Button BUTTON6 = buttons[1][2];
	Button BUTTON7 = buttons[2][0];
	Button BUTTON8 = buttons[2][1];
	Button BUTTON9 = buttons[2][2];
	Button BUTTONDOT = buttons[3][1];
	Button BUTTONBACK = buttons[3][2];
	Button BUTTONPLUS = buttons[0][3];
	Button BUTTONSUB = buttons[1][3];
	Button BUTTONMUL = buttons[2][3];
	Button BUTTONDIV = buttons[3][3];
	Button BUTTONEQUAL = buttons[4][3];
	



	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
        populateButtons();
    }


    private void populateButtons() {

    	TableLayout table = (TableLayout) findViewById(R.id.tableForButtons);


    	
    	for (int row = 0; row < NUM_ROWS; row++) {
    		TableRow tableRow = new TableRow(this);
    		tableRow.setLayoutParams(new TableLayout.LayoutParams(
    				TableLayout.LayoutParams.MATCH_PARENT,
    				TableLayout.LayoutParams.MATCH_PARENT,
    				1.0f));
    		table.addView(tableRow);
    		
    		for (int col = 0; col < NUM_COLS; col++){ 
    			final int FINAL_COL = col;
    			final int FINAL_ROW = row;
    			
    			Button button = new Button(this);
    			button.setLayoutParams(new TableRow.LayoutParams(
    					TableRow.LayoutParams.MATCH_PARENT,
    					TableRow.LayoutParams.MATCH_PARENT,
    					1.0f));
    			
    			button.setText(getButtonText(row,col));
    			button.setTextColor(0xffffffff);
    			button.setTextSize(24);
    			// Make text not clip on small buttons
    			button.setPadding(0, 0, 0, 0);
    			
    			button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

	    				gridButtonClicked(FINAL_COL, FINAL_ROW);
					}
				});
    			
    			tableRow.addView(button);
    			buttons[row][col] = button;
    		}
    	}
	}

    private void gridButtonClicked(int col, int row) {
    	TextView result = (TextView) findViewById(R.id.result);    	
    	//Toast.makeText(this, "Button clicked: " + getButtonText(row,col),
    	//		Toast.LENGTH_SHORT).show();
    	//Button button = buttons[row][col];
    	
    	int key = getKey(row,col);
    	
    	if(key==KEYDOT)
    	{
          if(state==-1 || state==0)
    	  {
    		leftdec =true;
    	  }
          if(state == 1 || state==2)
    	  {
    		rightdec =true;
    	  }
    	}

    	if(isKeyDigit(key) || (key==KEYDOT) )
    	{
	    	if(state==-1 || state==0)
	    	{
	    		leftnumstr  = leftnumstr + getButtonText(row,col);
	    		state = 0;
	    	}   	
	    	if(state == 1 || state==2)
	    	{
	    		rightnumstr  = rightnumstr + getButtonText(row,col);
	    		state=2;
	    	}
    	}
    	

    	// Read function
    	if((state==0 || state==1)  && isKeyFunc(key))
    	{
    	  func = key;
    	  state = 1;
    	} 

    	// Calculate
    	if(state==2  && (key==KEYEQUAL))
    	{
            double leftnum = Double.parseDouble(leftnumstr);
            double rightnum = Double.parseDouble(rightnumstr);
    		
            if ( func == KEYPLUS)
            {
            	resultnum = leftnum+rightnum;
            }
            else if (func == KEYSUB)
            {
            	resultnum = leftnum-rightnum;
            }
            else if (func == KEYMUL)
            {
            	resultnum = leftnum*rightnum;
            }
            else if (func == KEYDIV)
            {
            	resultnum = leftnum/rightnum;
            }
            state=3;
    	} 
    	
    	// ERROR CONDITIONS
    	if((state==-1  && isKeyFunc(key)) || (key==KEYCLEAR)) 
    	{
    	  func = -1;
    	  leftnumstr = "";
    	  rightnumstr = "";
    	  state = -1;
    	} 
    	
    	
    	// Lock Button Sizes:
    	lockButtonSizes();
    	
    	// Does not scale image.
//    	button.setBackgroundResource(R.drawable.action_lock_pink);
    	
    	// Scale image to button: Only works in JellyBean!
    	// Image from Crystal Clear icon set, under LGPL
    	// http://commons.wikimedia.org/wiki/Crystal_Clear
		/*int newWidth = button.getWidth();
		int newHeight = button.getHeight();
		Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.action_lock_pink);
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
		Resources resource = getResources();
		//button.setBackground(new BitmapDrawable(resource, scaledBitmap));
		*/
    	if(state==-1)
    	{
    		result.setText("");
    	}
    	else if(state==0 || state==1)
    	{
    		result.setText("" + leftnumstr);
    	}
    	else if(state==2)
    	{
    		result.setText("" + rightnumstr);
    	}
    	else if(state==3)
    	{
    		//result.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,android.R.anim.slide_out_right));
    		if(resultnum == (double)((int)(resultnum)) )
    		  result.setText("" + ((int)resultnum));
    		else
    	      result.setText("" + resultnum);
    		
    	}
    	
		
		// Change text on button:
		//button.setText("" + col);

    }

	private void lockButtonSizes() {
    	for (int row = 0; row < NUM_ROWS; row++) {
    		for (int col = 0; col < NUM_COLS; col++) {
    			Button button = buttons[row][col];
    			
    			int width = button.getWidth();
    			button.setMinWidth(width);
    			button.setMaxWidth(width);
    			
				int height = button.getHeight();
				button.setMinHeight(height);
				button.setMaxHeight(height);
    		}
    	}		
	}

	private String getButtonText(int row, int col) {
				
		if( (row == 3) && (col == 0))
		{
		   return "0";
		}
		if( (row == 0) && (col == 0))
		{
		   return "1";
		}
		if( (row == 0) && (col == 1))
		{
		   return "2";
		}
		if( (row == 0) && (col == 2))
		{
		   return "3";
		}
		if( (row == 1) && (col == 0))
		{
		   return "4";
		}
		if( (row == 1) && (col == 1))
		{
		   return "5";
		}
		if( (row == 1) && (col == 2))
		{
		   return "6";
		}
		if( (row == 2) && (col == 0))
		{
		   return "7";
		}
		if( (row == 2) && (col == 1))
		{
		   return "8";
		}
		if( (row == 2) && (col == 2))
		{
		   return "9";
		}
		if( (row == 3) && (col == 1))
		{
		   return ".";
		}
		if( (row == 3) && (col == 2))
		{
		   return "00";
		}
		if( (row == 0) && (col == 3))
		{
		   return "+";
		}
		if( (row == 1) && (col == 3))
		{
		   return "-";
		}
		if( (row == 2) && (col == 3))
		{
		   return "X";
		}
		if( (row == 3) && (col == 3))
		{
		   return "/";
		}
		if( (row == 4) && (col == 3))
		{
		   return "=";
		}
		if( (row == 4) && (col == 0))
		{
		   return "C";
		}
		if( (row == 4) && (col == 1))
		{
		   return "+/-";
		}
		if( (row == 4) && (col == 2))
		{
		   return "<";
		}
		return "error";
	}

	private int getKey(int row, int col) {
		
		if( (row == 3) && (col == 0))
		{
		   return KEY0;
		}
		if( (row == 0) && (col == 0))
		{
		   return KEY1;
		}
		if( (row == 0) && (col == 1))
		{
		   return KEY2;
		}
		if( (row == 0) && (col == 2))
		{
		   return KEY3;
		}
		if( (row == 1) && (col == 0))
		{
		   return KEY4;
		}
		if( (row == 1) && (col == 1))
		{
		   return KEY5;
		}
		if( (row == 1) && (col == 2))
		{
		   return KEY6;
		}
		if( (row == 2) && (col == 0))
		{
		   return KEY7;
		}
		if( (row == 2) && (col == 1))
		{
		   return KEY8;
		}
		if( (row == 2) && (col == 2))
		{
		   return KEY9;
		}
		if( (row == 3) && (col == 1))
		{
		   return KEYDOT;
		}
		if( (row == 4) && (col == 2))
		{
		   return KEYBACK;
		}
		if( (row == 0) && (col == 3))
		{
		   return KEYPLUS;
		}
		if( (row == 1) && (col == 3))
		{
		   return KEYSUB;
		}
		if( (row == 2) && (col == 3))
		{
		   return KEYMUL;
		}
		if( (row == 3) && (col == 3))
		{
		   return KEYDIV;
		}
		if( (row == 4) && (col == 3))
		{
		   return KEYEQUAL;
		}
		if( (row == 4) && (col == 0))
		{
		   return KEYCLEAR;
		}		
		return -1;
	}
	
	public boolean isKeyDigit(int key) {
		if(key>= 0 && key<=9)
			return true;
		else
			return false;
	}
	public boolean isKeyFunc(int key) {
		if(key>= 12 && key<=15)
			return true;
		else
			return false;
	}	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
