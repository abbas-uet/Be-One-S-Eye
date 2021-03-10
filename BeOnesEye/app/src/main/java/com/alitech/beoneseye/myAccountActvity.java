package com.alitech.beoneseye;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import com.alitech.beoneseye.TableLayout.Cell;
import com.alitech.beoneseye.TableLayout.ColumnHeader;
import com.alitech.beoneseye.TableLayout.MyTableViewAdapter;
import com.alitech.beoneseye.TableLayout.RowHeader;
import com.evrencoskun.tableview.TableView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static java.security.AccessController.getContext;

public class myAccountActvity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private List<RowHeader> mRowHeaderList;
    private List<ColumnHeader> mColumnHeaderList;
    private List<List<Cell>> mCellList;
    TableView tableView;
    Button history,loginH;
    DBHelper dbHelper;
    TextView name,email,location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_actvity);

        InitializeViews();
        MyTableViewAdapter adapter = new MyTableViewAdapter(getContext());
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColumnHeaderList.clear();
                mRowHeaderList.clear();
                mCellList.clear();
                ColumnHeader ColumnHeader1=new ColumnHeader("Action");
                ColumnHeader ColumnHeader2=new ColumnHeader("Time");
                ColumnHeader ColumnHeader3=new ColumnHeader("Text Extracted");
                ColumnHeader ColumnHeader4=new ColumnHeader("File Path");
                ColumnHeader ColumnHeader5=new ColumnHeader("FeedBack");
                mColumnHeaderList.add(ColumnHeader1);
                mColumnHeaderList.add(ColumnHeader2);
                mColumnHeaderList.add(ColumnHeader3);
                mColumnHeaderList.add(ColumnHeader4);
                mColumnHeaderList.add(ColumnHeader5);
                ArrayList<History> arrayList=dbHelper.getHistory(currentUser.getUid());
                for (History row:arrayList) {
                    Cell action=new Cell(row.getACTION());
                    Cell time=new Cell(row.getDATETIME());
                    Cell text=new Cell(row.getTEXT());
                    Cell filePath=new Cell(row.getIMAGE_PATH());
                    Cell feedback=new Cell(row.getFEEDBACK());
                    List<Cell>cellRow=new ArrayList<Cell>();
                    cellRow.add(action);
                    cellRow.add(time);
                    cellRow.add(text);
                    cellRow.add(filePath);
                    cellRow.add(feedback);
                    mCellList.add(cellRow);
                }
                tableView.setAdapter(adapter);
                adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
            }
        });

        loginH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mColumnHeaderList.clear();
                mRowHeaderList.clear();
                mCellList.clear();
                ColumnHeader ColumnHeader1=new ColumnHeader("Login Time");
                ColumnHeader ColumnHeader2=new ColumnHeader("LogOut Time");
                mColumnHeaderList.add(ColumnHeader1);
                mColumnHeaderList.add(ColumnHeader2);
                ArrayList<List<String>> arrayList=dbHelper.getLogiNH(currentUser.getUid());
                for (List<String> row:arrayList) {
                    Cell login=new Cell(row.get(0));
                    Cell logout=new Cell(row.get(1));
                    List<Cell>cellRow=new ArrayList<Cell>();
                    cellRow.add(login);
                    cellRow.add(logout);
                    mCellList.add(cellRow);
                }
                tableView.setAdapter(adapter);
                adapter.setAllItems(mColumnHeaderList, mRowHeaderList, mCellList);
            }
        });
    }

    private void InitializeViews() {
        dbHelper=new DBHelper(this);
        name=(TextView) findViewById(R.id.myName);
        location=(TextView) findViewById(R.id.myLocation);
        email=(TextView) findViewById(R.id.myEmail);
        history=(Button)findViewById(R.id.activityHistory);
        loginH=(Button)findViewById(R.id.loginHistory);
        tableView=(TableView)findViewById(R.id.content_container);
        mColumnHeaderList=new ArrayList<ColumnHeader>();
        mRowHeaderList=new ArrayList<RowHeader>();
        mCellList= new ArrayList<List<Cell>>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=mAuth.getCurrentUser();
        if(currentUser==null){
            new CustomToast().Show_Toast(this, new View(this),
                    "You Have not loged In. Please Login");
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    startActivity(new Intent(myAccountActvity.this,camerGallery.class));
                    finish();
                }
            }, 2000);
        }else{
            ArrayList<String> arrayList=dbHelper.getuser(currentUser.getUid());
            if(!arrayList.isEmpty()){
                name.setText(arrayList.get(1));
                email.setText(arrayList.get(2));
                location.setText(arrayList.get(3));
            }

        }

    }
}