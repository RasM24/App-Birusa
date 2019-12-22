package endroad.birusa;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import endroad.birusa.model.Message;
import ru.endroad.birusa.R;
import ru.endroad.birusa.component.session.model.User;

public class ViewHolders {

    public static class MessageHolder extends RecyclerView.ViewHolder {

        private final TextView mTextField;
        private final TextView mDateField;
        private final TextView mDateFieldMyself;
        private final TextView mAuthorFieldText;

        private String author;
        private final CardView mCardView;

        public String getAuthor() {
            return author;
            //return  mAuthorFieldText.getText();
        }


        public MessageHolder(View itemView) {
            super(itemView);
            mTextField = (TextView) itemView.findViewById(R.id.view_text);
            mDateField = (TextView) itemView.findViewById(R.id.view_date);
            mDateFieldMyself = (TextView) itemView.findViewById(R.id.view_date_myself);
            mCardView = (CardView) itemView.findViewById(R.id.view_card);
            mAuthorFieldText = (TextView) itemView.findViewById(R.id.view_author_text);

        }

        public void bindData(Context context, Message message, View.OnClickListener onClickListener) {

            if (FirebaseAuth.getInstance().getCurrentUser().getUid().compareTo(message.getUid()) == 0) {

                mDateField.setVisibility(View.GONE);
                mDateFieldMyself.setVisibility(View.VISIBLE);
                mDateFieldMyself.setText(message.getDateText());

                setAvatar(message.getUid());

            } else {

                mDateFieldMyself.setVisibility(View.GONE);
                mDateField.setVisibility(View.VISIBLE);
                mDateField.setText(message.getDateText());

                setAvatar(message.getUid());
                mCardView.setOnClickListener(onClickListener);
            }

            mTextField.setText(message.getText());

        }


        void setAvatar(String userId) {
            FirebaseDatabase.getInstance().getReference().child("users").child(userId).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            User user = dataSnapshot.getValue(User.class);
                            if (user == null) {
                                return;
                            }
                            mAuthorFieldText.setText(user.getNick());
                            author = user.getNick();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
        }

    }
}