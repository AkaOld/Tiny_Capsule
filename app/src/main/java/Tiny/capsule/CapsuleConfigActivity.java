package Tiny.capsule;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import Tiny.capsule.model.Global;
import de.hdodenhof.circleimageview.CircleImageView;

public class CapsuleConfigActivity extends AppCompatActivity {

    private ImageView back;               //返回上一层
    private PieChart mchart;              //统计图表
    private CircleImageView Invite;       //邀请好友加入胶囊
    private CircleImageView Remove;       //踢出好友
    private EditText Capsulename;         //胶囊名称
    private EditText nickname;            //我在胶囊中的昵称
    private ImageView changeback;         //更改胶囊背景
    private TextView Save;                //保存更改
    private TextView Cname;               //胶囊名称

    private ImageView image_friend1;      //胶囊成员
    private ImageView image_friend2;      //胶囊成员
    private ImageView image_friend3;      //胶囊成员
    private ImageView image_friend4;      //胶囊成员

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capsule_config);

        image_friend1= findViewById(R.id.image_friend1);
        image_friend2= findViewById(R.id.image_friend2);
        image_friend3= findViewById(R.id.image_friend3);
        image_friend4= findViewById(R.id.image_friend4);
//        image_friend1.setVisibility(ImageView.INVISIBLE);
//        image_friend2.setVisibility(ImageView.INVISIBLE);
//        image_friend3.setVisibility(ImageView.INVISIBLE);
//        image_friend4.setVisibility(ImageView.INVISIBLE);
//        switch (Global.icons.size()){
//            case 4:image_friend4.setVisibility(ImageView.INVISIBLE);String path = FileUtil.getUserIconFilePath(Global.icons.get(3));
//                Bitmap bitmap = getLoacalBitmap(path);
//                image_friend4.setImageBitmap(bitmap);
//            case 3:image_friend3.setVisibility(ImageView.INVISIBLE);String path1 = FileUtil.getUserIconFilePath(Global.icons.get(2));
//                Bitmap bitmap1 = getLoacalBitmap(path1);
//                image_friend4.setImageBitmap(bitmap1);
//            case 2:image_friend2.setVisibility(ImageView.INVISIBLE);String path2 = FileUtil.getUserIconFilePath(Global.icons.get(1));
//                Bitmap bitmap2 = getLoacalBitmap(path2);
//                image_friend4.setImageBitmap(bitmap2);
//            case 1:image_friend1.setVisibility(ImageView.INVISIBLE);String path3 = FileUtil.getUserIconFilePath(Global.icons.get(0));
//                Bitmap bitmap3 = getLoacalBitmap(path3);
//                image_friend4.setImageBitmap(bitmap3);
//        }

        back = findViewById(R.id.toolbar2_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Cname = findViewById(R.id.textView8);
        Cname.setText(getIntent().getStringExtra("title"));

        mchart = findViewById(R.id.Piechart);
        PieData mPieData = getPieData(4, 100);
        showChart(mchart, mPieData);

        Invite = findViewById(R.id.invite);
        Remove = findViewById(R.id.remove);
        //changeback = findViewById(R.id.imageview1);
        Save = findViewById(R.id.save);
    }

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        //pieChart.setHoleColorTransparent(true);

        pieChart.setDrawHoleEnabled(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

        //pieChart.setDescription();

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText("年龄分布");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

    /**
     *
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        for (int i = 0; i < count; i++) {
            xValues.add("2" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        }

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        float quarterly1 = 25;
        float quarterly2 = 25;
        float quarterly3 = 25;
        float quarterly4 = 25;

        yValues.add(new PieEntry(quarterly1, 0));
        yValues.add(new PieEntry(quarterly2, 1));
        yValues.add(new PieEntry(quarterly3, 2));
        yValues.add(new PieEntry(quarterly4, 3));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "20 21 22 23"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(pieDataSet);

        return pieData;
    }

}
