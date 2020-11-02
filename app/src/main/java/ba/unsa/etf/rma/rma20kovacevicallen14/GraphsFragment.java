package ba.unsa.etf.rma.rma20kovacevicallen14;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphsFragment extends Fragment {

    public GraphsFragment() {
        // Required empty public constructor
    }

    private BarChart barChart1;
    private BarChart barChart2;
    private BarChart barChart3;
    private Button monthButton;
    private Button weekButton;
    private Button dayButton;

    private OnSwipeAction onSwipeAction;
    public interface OnSwipeAction {
        public void onSwipeLeftGraph();
        public void onSwipeRightGraph();
    }

    private GraphsPresenter presenter = new GraphsPresenter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphs, container, false);

        monthButton = view.findViewById(R.id.monthOption);
        weekButton = view.findViewById(R.id.weekOption);
        dayButton = view.findViewById(R.id.dayOption);

        onSwipeAction = (OnSwipeAction) getActivity();
        view.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
            @Override
            public void onSwipeLeft() {
                onSwipeAction.onSwipeLeftGraph();
            }
            @Override
            public void onSwipeRight() {
                onSwipeAction.onSwipeRightGraph();
            }
        });

        barChart1 = view.findViewById(R.id.bargraph1);

        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
        barEntries1.add(new BarEntry(1, presenter.getMonthIncome(0)));
        barEntries1.add(new BarEntry(2, presenter.getMonthIncome(1)));
        barEntries1.add(new BarEntry(3, presenter.getMonthIncome(2)));
        barEntries1.add(new BarEntry(4, presenter.getMonthIncome(3)));
        barEntries1.add(new BarEntry(5, presenter.getMonthIncome(4)));
        barEntries1.add(new BarEntry(6, presenter.getMonthIncome(5)));
        barEntries1.add(new BarEntry(7, presenter.getMonthIncome(6)));
        barEntries1.add(new BarEntry(8, presenter.getMonthIncome(7)));
        barEntries1.add(new BarEntry(9, presenter.getMonthIncome(8)));
        barEntries1.add(new BarEntry(10, presenter.getMonthIncome(9)));
        barEntries1.add(new BarEntry(11, presenter.getMonthIncome(10)));
        barEntries1.add(new BarEntry(12, presenter.getMonthIncome(11)));
        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Income");
        barDataSet1.setColor(ColorTemplate.rgb("00FF00"));

        BarData barData1 = new BarData(barDataSet1);
        barChart1.setData(barData1);

        barChart2 = view.findViewById(R.id.bargraph2);

        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
        barEntries2.add(new BarEntry(1, presenter.getMonthOutcome(0)));
        barEntries2.add(new BarEntry(2, presenter.getMonthOutcome(1)));
        barEntries2.add(new BarEntry(3, presenter.getMonthOutcome(2)));
        barEntries2.add(new BarEntry(4, presenter.getMonthOutcome(3)));
        barEntries2.add(new BarEntry(5, presenter.getMonthOutcome(4)));
        barEntries2.add(new BarEntry(6, presenter.getMonthOutcome(5)));
        barEntries2.add(new BarEntry(7, presenter.getMonthOutcome(6)));
        barEntries2.add(new BarEntry(8, presenter.getMonthOutcome(7)));
        barEntries2.add(new BarEntry(9, presenter.getMonthOutcome(8)));
        barEntries2.add(new BarEntry(10, presenter.getMonthOutcome(9)));
        barEntries2.add(new BarEntry(11, presenter.getMonthOutcome(10)));
        barEntries2.add(new BarEntry(12, presenter.getMonthOutcome(11)));
        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Outcome");
        barDataSet2.setColor(ColorTemplate.rgb("FF0000"));

        BarData barData2 = new BarData(barDataSet2);
        barChart2.setData(barData2);

        barChart3 = view.findViewById(R.id.bargraph3);

        ArrayList<BarEntry> barEntries3 = new ArrayList<>();
        barEntries3.add(new BarEntry(1, presenter.getTotal(0)));
        barEntries3.add(new BarEntry(2, presenter.getTotal(1)));
        barEntries3.add(new BarEntry(3, presenter.getTotal(2)));
        barEntries3.add(new BarEntry(4, presenter.getTotal(3)));
        barEntries3.add(new BarEntry(5, presenter.getTotal(4)));
        barEntries3.add(new BarEntry(6, presenter.getTotal(5)));
        barEntries3.add(new BarEntry(7, presenter.getTotal(6)));
        barEntries3.add(new BarEntry(8, presenter.getTotal(7)));
        barEntries3.add(new BarEntry(9, presenter.getTotal(8)));
        barEntries3.add(new BarEntry(10, presenter.getTotal(9)));
        barEntries3.add(new BarEntry(11, presenter.getTotal(10)));
        barEntries3.add(new BarEntry(12, presenter.getTotal(11)));
        BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Total");
        barDataSet3.setColor(ColorTemplate.rgb("FFA500"));

        BarData barData3 = new BarData(barDataSet3);
        barChart3.setData(barData3);

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BarEntry> barEntries1 = new ArrayList<>();
                barEntries1.add(new BarEntry(1, presenter.getMonthIncome(0)));
                barEntries1.add(new BarEntry(2, presenter.getMonthIncome(1)));
                barEntries1.add(new BarEntry(3, presenter.getMonthIncome(2)));
                barEntries1.add(new BarEntry(4, presenter.getMonthIncome(3)));
                barEntries1.add(new BarEntry(5, presenter.getMonthIncome(4)));
                barEntries1.add(new BarEntry(6, presenter.getMonthIncome(5)));
                barEntries1.add(new BarEntry(7, presenter.getMonthIncome(6)));
                barEntries1.add(new BarEntry(8, presenter.getMonthIncome(7)));
                barEntries1.add(new BarEntry(9, presenter.getMonthIncome(8)));
                barEntries1.add(new BarEntry(10, presenter.getMonthIncome(9)));
                barEntries1.add(new BarEntry(11, presenter.getMonthIncome(10)));
                barEntries1.add(new BarEntry(12, presenter.getMonthIncome(11)));
                BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Income");
                barDataSet1.setColor(ColorTemplate.rgb("00FF00"));
                BarData barData1 = new BarData(barDataSet1);
                barChart1.setData(barData1);
                barChart1.invalidate();

                ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                barEntries2.add(new BarEntry(1, presenter.getMonthOutcome(0)));
                barEntries2.add(new BarEntry(2, presenter.getMonthOutcome(1)));
                barEntries2.add(new BarEntry(3, presenter.getMonthOutcome(2)));
                barEntries2.add(new BarEntry(4, presenter.getMonthOutcome(3)));
                barEntries2.add(new BarEntry(5, presenter.getMonthOutcome(4)));
                barEntries2.add(new BarEntry(6, presenter.getMonthOutcome(5)));
                barEntries2.add(new BarEntry(7, presenter.getMonthOutcome(6)));
                barEntries2.add(new BarEntry(8, presenter.getMonthOutcome(7)));
                barEntries2.add(new BarEntry(9, presenter.getMonthOutcome(8)));
                barEntries2.add(new BarEntry(10, presenter.getMonthOutcome(9)));
                barEntries2.add(new BarEntry(11, presenter.getMonthOutcome(10)));
                barEntries2.add(new BarEntry(12, presenter.getMonthOutcome(11)));
                BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Outcome");
                barDataSet2.setColor(ColorTemplate.rgb("FF0000"));
                BarData barData2 = new BarData(barDataSet2);
                barChart2.setData(barData2);
                barChart2.invalidate();

                ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                barEntries3.add(new BarEntry(1, presenter.getTotal(0)));
                barEntries3.add(new BarEntry(2, presenter.getTotal(1)));
                barEntries3.add(new BarEntry(3, presenter.getTotal(2)));
                barEntries3.add(new BarEntry(4, presenter.getTotal(3)));
                barEntries3.add(new BarEntry(5, presenter.getTotal(4)));
                barEntries3.add(new BarEntry(6, presenter.getTotal(5)));
                barEntries3.add(new BarEntry(7, presenter.getTotal(6)));
                barEntries3.add(new BarEntry(8, presenter.getTotal(7)));
                barEntries3.add(new BarEntry(9, presenter.getTotal(8)));
                barEntries3.add(new BarEntry(10, presenter.getTotal(9)));
                barEntries3.add(new BarEntry(11, presenter.getTotal(10)));
                barEntries3.add(new BarEntry(12, presenter.getTotal(11)));
                BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Total");
                barDataSet3.setColor(ColorTemplate.rgb("FFA500"));
                BarData barData3 = new BarData(barDataSet3);
                barChart3.setData(barData3);
                barChart3.invalidate();
            }
        });

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BarEntry> barEntries1 = new ArrayList<>();
                for(int i = 1; i <= 31; i++) {
                    barEntries1.add(new BarEntry(i, presenter.getDayIncome(i)));
                }
                BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Income");
                barDataSet1.setColor(ColorTemplate.rgb("00FF00"));
                BarData barData1 = new BarData(barDataSet1);
                barChart1.setData(barData1);
                barChart1.invalidate();

                ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                for(int i = 1; i <= 31; i++) {
                    barEntries2.add(new BarEntry(i, presenter.getDayOutcome(i)));
                }
                BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Outcome");
                barDataSet2.setColor(ColorTemplate.rgb("FF0000"));
                BarData barData2 = new BarData(barDataSet2);
                barChart2.setData(barData2);
                barChart2.invalidate();

                ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                for(int i = 1; i <= 31; i++) {
                    barEntries3.add(new BarEntry(i, presenter.getDayTotal(i)));
                }
                BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Total");
                barDataSet3.setColor(ColorTemplate.rgb("FFA500"));
                BarData barData3 = new BarData(barDataSet3);
                barChart3.setData(barData3);
                barChart3.invalidate();
            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BarEntry> barEntries1 = new ArrayList<>();
                //idemo 6 puta kroz petlju zbog Calendar klase koja definiše da ima 6 sedmica u mjesecu
                for(int i = 1; i <= 6; i++) {
                    barEntries1.add(new BarEntry(i, presenter.getWeekIncome(i)));
                }
                BarDataSet barDataSet1 = new BarDataSet(barEntries1, "Income");
                barDataSet1.setColor(ColorTemplate.rgb("00FF00"));
                BarData barData1 = new BarData(barDataSet1);
                barChart1.setData(barData1);
                barChart1.invalidate();

                ArrayList<BarEntry> barEntries2 = new ArrayList<>();
                //idemo 6 puta kroz petlju zbog Calendar klase koja definiše da ima 6 sedmica u mjesecu
                for(int i = 1; i <= 6; i++) {
                    barEntries2.add(new BarEntry(i, presenter.getWeekOutcome(i)));
                }
                BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Outcome");
                barDataSet2.setColor(ColorTemplate.rgb("FF0000"));
                BarData barData2 = new BarData(barDataSet2);
                barChart2.setData(barData2);
                barChart2.invalidate();

                ArrayList<BarEntry> barEntries3 = new ArrayList<>();
                //idemo 6 puta kroz petlju zbog Calendar klase koja definiše da ima 6 sedmica u mjesecu
                for(int i = 1; i <= 6; i++) {
                    barEntries3.add(new BarEntry(i, presenter.getWeekTotal(i)));
                }
                BarDataSet barDataSet3 = new BarDataSet(barEntries3, "Total");
                barDataSet3.setColor(ColorTemplate.rgb("FFA500"));
                BarData barData3 = new BarData(barDataSet3);
                barChart3.setData(barData3);
                barChart3.invalidate();
            }
        });


        return view;
    }
}
