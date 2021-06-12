package com.naserkarimi.bottomsheetviewsample;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.naserkarimi.custombottomsheet.CustomBottomSheetDialog;
import com.naserkarimi.custombottomsheet.StateChangedCallback;

public class MainActivity extends AppCompatActivity {

    Button buttonForBottom;
    LinearLayout layoutForTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void createCustomBottomSheet() {
        createUi();
        float peekHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250F, getResources().getDisplayMetrics());
        CustomBottomSheetDialog customBottomSheet = new CustomBottomSheetDialog.Builder(this,layoutForTop)
                .style(R.style.TransparentBottomSheetDialog)
                .bottomView(buttonForBottom)
                .isCancelable(true)
                .peekHeight((int) peekHeight)
                .stateChangedCallback(new StateChangedCallback() {
                    @Override
                    public void onHidden() {

                    }

                    @Override
                    public void onCollapsed() {

                    }

                    @Override
                    public void onExpanded() {

                    }
                })
                .build();
        customBottomSheet.show();
    }

    private void createUi() {
        buttonForBottom = new Button(this);
        buttonForBottom.setText("action");
        buttonForBottom.setBackgroundColor(Color.YELLOW);
        buttonForBottom.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LayoutInflater inflater = LayoutInflater.from(this);
        layoutForTop = (LinearLayout) inflater.inflate(R.layout.bottom_sheet_behavior, null, false);
    }

    public void createCustomBottomSheet(View view) {
        createCustomBottomSheet();
    }
}