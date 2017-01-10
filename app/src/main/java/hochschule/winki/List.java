package hochschule.winki;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
;

/**
 * Created by danielf on 19.11.2016.
 */

public class List extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final Activity context;
    private final String[] subjects;
    private View rowView;
    private AdapterView av;


    public List(Activity context, String[] subjects) {
        super(context, R.layout.list_row, subjects);
        this.context = context;
        this.subjects = subjects;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        rowView = inflater.inflate(R.layout.list_row, null, true);
        TextView subject = (TextView) rowView.findViewById(R.id.subjectListRow);
        subject.setText(subjects[position]);
        return rowView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
