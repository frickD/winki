package hochschule.winki;

import android.app.Activity;
import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Hollinger on 20.12.2016.
 */

public class SubjectList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] items;
    View rowView;

    public SubjectList(Activity context, String[] items) {
        super(context, R.layout.list_row, items);
        this.context = context;
        this.items = items;
    }

    // einfach nur aus SemesterList kopiert
    @Override
    public View getView (int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.list_row, null, true);
        TextView subject = (TextView) rowView.findViewById(R.id.subjectListRow);
        subject.setText(items[position]);
        return rowView;
    }

}
