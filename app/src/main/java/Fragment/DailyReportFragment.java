package Fragment;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.haitr.planed_12062016.LoginActivity;
import com.example.haitr.planed_12062016.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import Class.PieChartData;
import Controller.Report_Control;
import utilities.DemoBase;
import Class.BarChartData;

/**
 * Created by Thanh Huy on 7/29/2016.
 */
public class DailyReportFragment extends DemoBase implements OnChartValueSelectedListener {
    protected BarChart barChart;
    protected PieChart fullPieChart;
    protected PieChart halfPieChart;
    protected ArrayList<Integer> halfpieArray;
    protected ArrayList<PieChartData> fullpieArray;
    protected ArrayList<BarChartData> BarchartArray;
    protected int fullpieCount;
    protected Report_Control report_control;
    protected int accountID;
    protected String sTotal;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_report, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ;
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        halfPieChart = (PieChart) getActivity().findViewById(R.id.halfpie);
        fullPieChart = (PieChart) getActivity().findViewById(R.id.fullpie);
        barChart = (BarChart) getActivity().findViewById(R.id.BarChart);
        accountID = LoginActivity.Account_Id;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        report_control = new Report_Control(LoginActivity.db.getConnection());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadData_HalfPie().execute();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadData_FullPie().execute();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new LoadData_BarChart().execute();
            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void moveOffScreen() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated
        int offset = (int) (height * 0.65); /* percent to move */
        LinearLayout.LayoutParams rlParams =
                (LinearLayout.LayoutParams) halfPieChart.getLayoutParams();
        rlParams.setMargins(0, 0, 0, -offset);
        halfPieChart.setLayoutParams(rlParams);
    }

    private SpannableString generateCenterSpannableText_Half() {
        SpannableString s = new SpannableString("Total Tasks Planned: " + sTotal);
        s.setSpan(new RelativeSizeSpan(1.2f), 0, 22, 0);
        s.setSpan(new StyleSpan(Typeface.BOLD), 22, s.length(), 0);
        return s;
    }

    private void HalfPie_setData(int count, float range) {
        ArrayList<PieEntry> values = new ArrayList<PieEntry>();
        String[] string = {"Done", "Not Done"};
        for (int i = 0; i < count; i++) {
            values.add(new PieEntry((float) halfpieArray.get(i + 1), string[i] + ": " + halfpieArray.get(i + 1)));
        }
        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        halfPieChart.setData(data);
        halfPieChart.invalidate();
    }

    public void Styling_HalfPie() {
        halfPieChart.setBackgroundColor(Color.WHITE);
        halfPieChart.setUsePercentValues(true);
        halfPieChart.setDescription("");
        halfPieChart.setCenterTextTypeface(mTfLight);
        halfPieChart.setCenterText(generateCenterSpannableText_Half());
        halfPieChart.setDrawHoleEnabled(true);
        halfPieChart.setHoleColor(Color.WHITE);
        halfPieChart.setTransparentCircleColor(Color.WHITE);
        halfPieChart.setTransparentCircleAlpha(110);
        halfPieChart.setHoleRadius(58f);
        halfPieChart.setTransparentCircleRadius(61f);
        halfPieChart.setDrawCenterText(true);
        halfPieChart.setRotationEnabled(false);
        halfPieChart.setHighlightPerTapEnabled(true);
        halfPieChart.setMaxAngle(180f); // HALF CHART
        halfPieChart.setRotationAngle(180f);
        halfPieChart.setCenterTextOffset(0, -20);
        HalfPie_setData(2, 100);
        halfPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend l = halfPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // entry label styling
        halfPieChart.setEntryLabelColor(Color.BLACK);
        halfPieChart.setEntryLabelTypeface(mTfRegular);
        halfPieChart.setEntryLabelTextSize(12f);
    }

    public void Styling_BarChart() {
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        Legend l = barChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setTypeface(mTfLight);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xl = barChart.getXAxis();
        xl.setTypeface(mTfLight);
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        xl.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        //leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)
        barChart.getAxisRight().setEnabled(false);
        BarChart_SetData();

    }

    public void BarChart_SetData() {
        float groupSpace = 0.4f;
        float barSpace = 0.3f; // x3 dataset
        float barWidth = 1.5f; // x3 dataset
        // (0.3 + 0.02) * 3 + 0.04 = 1.00 -> interval per "group"

        int startYear = 0;
        //  int endYear = startYear + 4;

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        // ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();

        // float mult = 100 * 100000f;

        for (int i = startYear; i < BarchartArray.size(); i++) {
            float val = (float) BarchartArray.get(i).getiActTime();
            String labels = BarchartArray.get(i).getsName();
            yVals1.add(new BarEntry(i, val, labels));
        }

        for (int i = startYear; i < BarchartArray.size(); i++) {
            float val = (float) BarchartArray.get(i).getiEstTime();
            String labels = BarchartArray.get(i).getsName();
            yVals2.add(new BarEntry(i, val, labels));
        }


        BarDataSet set1, set2;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);

            set1.setValues(yVals1);
            set2.setValues(yVals2);

            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {

            set1 = new BarDataSet(yVals1, "Actual Time");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(yVals2, "Estimated Time");
            set2.setColor(Color.rgb(164, 228, 251));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);


            BarData data = new BarData(dataSets);
            data.setValueFormatter(new LargeValueFormatter());

            // add space between the dataset groups in percent of bar-width
            data.setValueTypeface(mTfLight);

            barChart.setData(data);
        }

        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinValue(startYear);
        barChart.getXAxis().setAxisMaxValue(barChart.getBarData().getGroupWidth(groupSpace, barSpace) * 6 + startYear);
        barChart.groupBars(startYear, groupSpace, barSpace);
        // barChart.invalidate();

    }

    public void Styling_FullPie() {
        fullPieChart.setBackgroundColor(Color.WHITE);
        fullPieChart.setUsePercentValues(true);
        fullPieChart.setDescription("");
        fullPieChart.setExtraOffsets(5, 10, 5, 5);
        fullPieChart.setDragDecelerationFrictionCoef(0.95f);
        fullPieChart.setCenterTextTypeface(mTfLight);
        fullPieChart.setCenterText(generateCenterSpannableText_Full());
        fullPieChart.setDrawHoleEnabled(true);
        fullPieChart.setHoleColor(Color.WHITE);
        fullPieChart.setTransparentCircleColor(Color.WHITE);
        fullPieChart.setTransparentCircleAlpha(110);
        fullPieChart.setHoleRadius(58f);
        fullPieChart.setTransparentCircleRadius(61f);
        fullPieChart.setDrawCenterText(true);
        fullPieChart.setRotationAngle(0);
        fullPieChart.setRotationEnabled(true);
        fullPieChart.setHighlightPerTapEnabled(true);

        fullPieChart.setOnChartValueSelectedListener(this);
        FullPie_Setdata(fullpieCount, 100);
        fullPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = fullPieChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        // entry label styling
        fullPieChart.setEntryLabelColor(Color.BLACK);
        fullPieChart.setEntryLabelTypeface(mTfRegular);
        fullPieChart.setEntryLabelTextSize(12f);
    }

    private void FullPie_Setdata(int count, float range) {

        float mult = range;
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count; i++) {
            entries.add(new PieEntry((float) fullpieArray.get(i).getTime(), fullpieArray.get(i).getName() + ": " + fullpieArray.get(i).getTime() + " minutes"));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfLight);
        fullPieChart.setData(data);
        // undo all highlights
        fullPieChart.highlightValues(null);
        fullPieChart.invalidate();
    }

    private SpannableString generateCenterSpannableText_Full() {

        SpannableString s = new SpannableString("MPAndroidChart\ndeveloped by Philipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    public class LoadData_HalfPie extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            halfpieArray = report_control.GetNumberOfTask(accountID, "2016-08-03");
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sTotal = halfpieArray.get(0).toString();
            Styling_HalfPie();
        }
    }

    public class LoadData_FullPie extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            fullpieArray = report_control.GetPieChartData(accountID, "2016-08-03");
            fullpieCount = report_control.GetPieSize(accountID, "2016-08-03");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Styling_FullPie();
        }
    }

    public class LoadData_BarChart extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            //Styling_BarChart();
            BarchartArray = report_control.GetDataBarChart(accountID, "2016-08-03");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Styling_BarChart();
        }
    }
}
