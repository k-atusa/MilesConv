package com.example.ku7_milesconv;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity implements ItemAdapter.OnItemActionListener {
    private ItemAdapter itemAdapter;
    private NoteIO note;
    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_list);
        note = new NoteIO(true, this);

        // set listeners
        ImageButton newPageButton = findViewById(R.id.new_page_button);
        newPageButton.setOnClickListener(v -> onNewPageClick());
        ImageButton resetButton = findViewById(R.id.change_pw_button);
        resetButton.setOnClickListener(v -> onResetClick());
        RecyclerView recyclerView = findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(this);
        recyclerView.setAdapter(itemAdapter);
        itemAdapter.items = Arrays.asList(note.titles);
        itemAdapter.updateItems();

        // set callback to autosort
        resultLauncher = registerForActivityResult (
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        note = new NoteIO(true, this);
                        note.sortNote();
                        note.saveNote();
                        itemAdapter.items = Arrays.asList(note.titles);
                        itemAdapter.updateItems();
                    }
                }
        );
    }

    @Override
    public void onItemClick(int pos) { // start edit activity
        Intent intent = new Intent(ListActivity.this, EditActivity.class);
        intent.putExtra("position", pos);
        resultLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(int pos) { // delete note
        showConfirmationDialog(pos, note.titles[pos]);
        itemAdapter.items = Arrays.asList(note.titles);
        itemAdapter.updateItems();
    }

    public void onNewPageClick() { // add new note
        note.titles = Arrays.copyOf(note.titles, note.titles.length + 1);
        note.contents = Arrays.copyOf(note.contents, note.contents.length + 1);
        note.titles[note.titles.length - 1] = "New note";
        note.contents[note.contents.length - 1] = "";
        note.saveNote();
        itemAdapter.items = Arrays.asList(note.titles);
        itemAdapter.updateItems();
    }

    // reset password
    public void onResetClick() {
        showNumberInputDialog();
    }
    private void showNumberInputDialog() {
        // create input dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Password");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("0000");

        // set layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = (int) (24 * getResources().getDisplayMetrics().density);
        params.setMargins(margin, 0, margin, 0);
        input.setLayoutParams(params);
        layout.addView(input);
        builder.setView(layout);

        // reset password
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String enteredText = input.getText().toString();
                if (enteredText.isEmpty()) {
                    note.showMsg("Invalid Password");
                    return;
                }
                try {
                    double number = Double.parseDouble(enteredText);
                    note.password = enteredText;
                    note.saveNote();
                } catch (Exception e) {
                    note.showMsg(e.toString());
                }
            }
        });

        // cancel work
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void showConfirmationDialog(int pos, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Note Delete");
        builder.setMessage("Delete note <" + title + ">?");

        // delete note
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] newTitles = new String[note.titles.length - 1];
                String[] newContents = new String[note.contents.length - 1];
                for (int i = 0; i < pos; i++) {
                    newTitles[i] = note.titles[i];
                    newContents[i] = note.contents[i];
                }
                for (int i = pos + 1; i < note.titles.length; i++) {
                    newTitles[i - 1] = note.titles[i];
                    newContents[i - 1] = note.contents[i];
                }
                note.titles = newTitles;
                note.contents = newContents;
                note.saveNote();
                itemAdapter.items = Arrays.asList(note.titles);
                itemAdapter.updateItems();
            }
        });

        // cancel work
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}