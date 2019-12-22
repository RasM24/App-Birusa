package endroad.birusa;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import endroad.birusa.model.Message;
import ru.endroad.birusa.R;

public class FragmentChat extends Fragment implements View.OnClickListener {

    RecyclerView recycler;
    FirebaseRecyclerAdapter mAdapter;
    ImageView mSubmitButton;
    EditText mTextField;
    DatabaseReference mRef;
    final static String DB_CHAT = "chat";
    final static int MAX_CHAT_MESSAGE = 100;

    public FragmentChat() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        mSubmitButton = (ImageView) rootView.findViewById(R.id.view_submit);
        mTextField = (EditText) rootView.findViewById(R.id.view_edit_text);
        rootView.findViewById(R.id.view_submit).setOnClickListener(this);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        recycler = (RecyclerView) rootView.findViewById(R.id.recycleList);
        //recycler.setHasFixedSize(true);
        recycler.setLayoutManager(llm);

        mRef = FirebaseDatabase.getInstance().getReference();

        Query query = mRef.child(DB_CHAT).limitToLast(MAX_CHAT_MESSAGE);
        mAdapter = new FirebaseRecyclerAdapter<Message, ViewHolders.MessageHolder>(Message.class, R.layout.item_message, ViewHolders.MessageHolder.class, query) {
            @Override
            public void populateViewHolder(final ViewHolders.MessageHolder vh, final Message message, int position) {
                vh.bindData(getContext(), message, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.view_card && ((BaseActivity)getActivity()).getUid().compareTo(message.getUid()) != 0)
                            if ((TextUtils.isEmpty(mTextField.getText().toString()))) {
                                mTextField.setText(vh.getAuthor() + ", ");
                                mTextField.setSelection(mTextField.getText().length());
                            }
                    }
                });
            }

            @Override
            protected void onDataChanged() {

                if (recycler.getAdapter().getItemCount() != 0)
                    recycler.smoothScrollToPosition(recycler.getAdapter().getItemCount() - 1);
            }

        };
        recycler.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.view_submit) {
            submitMessage();
        }
    }

    private void submitMessage() {

        final String text = mTextField.getText().toString();

        if (TextUtils.isEmpty(text)) {
            mTextField.setError("Введите текст");
            return;
        }

        setEditingEnabled(false);
        //writeNewPost(userId, text);
        //writeNewPost(text);

        final String userId = ((BaseActivity)getActivity()).getUid();
        writeNewPost(userId, text);

    }

    private void setEditingEnabled(boolean enabled) {
        //mTextField.setEnabled(enabled);
        mSubmitButton.setEnabled(enabled);
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String text) {

        String key = mRef.child(DB_CHAT).push().getKey();
        Message message = new Message(text, userId, BaseActivity.getTime());

        Map<String, Object> values = message.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(DB_CHAT + "/" + key, values);

        mRef.updateChildren(childUpdates);
        mTextField.setText("");
        setEditingEnabled(true);
    }

}
