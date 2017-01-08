package hochschule.winki;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
;

/**
 * Created by danielf on 19.11.2016.
 */

public class SemesterList extends ArrayAdapter<String> implements AdapterView.OnItemClickListener {

    private final Activity context;
    private final String[] subjects;
    private View rowView;
    private AdapterView av;


    public SemesterList(Activity context, String[] subjects) {
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



    // Das ist nur einmal eine Idee, wie man das Fach auslesen kann, auf das man geklickt hat.
    // Wie man hier jetzt weiter macht um das Layout von SubjectList, subjects.xml, aufzurufen, das weißt ich leider nicht.
    @Override
    public void onItemClick(AdapterView<?> adapterView, View rowView, int position, long id) {
        String semesterItem = (String) adapterView.getItemAtPosition(position);
        String[] subject = findSubject(semesterItem);

        //Toast.makeText(getActivity(), semesterItem, Toast.LENGTH_SHORT).show();
        SubjectList adapter = new SubjectList(context, subject);


        //lv.setAdapter(adapter);


    }

    private String[] findSubject(String item) {
        if (item == "Wirtschaftsinformatik 1")
            return Subjects.wirtschaftsinformatik1;
        else if (item == "Wirtschaftsinformatik 2")
            return Subjects.wirtschaftsinformatik2;
        else if (item == "Wirtschafsmathematik 1")
            return Subjects.wirtschaftsmathematik1;
        else return Subjects.wirtschaftsmathematik2;
    }

}
