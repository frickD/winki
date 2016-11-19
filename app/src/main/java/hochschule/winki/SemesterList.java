package hochschule.winki;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by danielf on 19.11.2016.
 */

public class SemesterList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] subjects;

    public SemesterList(Activity context, String[] subjects) {
        super(context, R.layout.list_row, subjects);
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public View getView (int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_row, null, true);
        TextView subject = (TextView) rowView.findViewById(R.id.subjectListRow);
        subject.setText(subjects[position]);
        return rowView;
    }
}
