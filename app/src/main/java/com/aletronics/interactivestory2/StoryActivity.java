package com.aletronics.interactivestory2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aletronics.interactivestory2.model.Page;
import com.aletronics.interactivestory2.model.Story;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoryActivity extends AppCompatActivity
{
    public static final String TAG = StoryActivity.class.getSimpleName();
    private Page[] mPages;
    private Story mStory = new Story();
    private Page mCurrentPage;
    //private ImageView mImageView;
    //private TextView mTextView;
    //private Button mChoice1;
    //private Button mChoice2;

    @BindView(R.id.orTextView) TextView mOrTextView;
    @BindView(R.id.storyImageView) ImageView mImageView;
    @BindView(R.id.storyTextView) TextView mTextView;
    @BindView(R.id.choiceButton1) Button mChoice1;
    @BindView(R.id.choiceButton2) Button mChoice2;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mName = intent.getStringExtra(getString(R.string.key_name));

        if(mName == null)
        {
            mName="Friend";
        }
        Log.d(TAG, mName);

        loadPage(0);
    }

    private void loadPage(int choice)
    {
        mCurrentPage = mStory.getPage(choice);

                            //ResourcesCompat.getDrawable(getResources(), mCurrentPage.getImageId(), null);
                            //  or
                            //getResources().getDrawable(mCurrentPage.getImageId());
        Drawable drawable = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = getDrawable(mCurrentPage.getImageId());
        } else {
            drawable = ResourcesCompat.getDrawable(getResources(), mCurrentPage.getImageId(), null);//getResources().getDrawable(mCurrentPage.getImageId());
        }

        String pageText = mCurrentPage.getText();
        //insert mName into storey text. Wont add if there is no placeholder
        pageText = String.format(pageText,mName);

        mTextView.setText(pageText);
        if(mCurrentPage.isFinal())
        {
            mOrTextView.setVisibility(View.INVISIBLE);
            mChoice1.setVisibility(View.INVISIBLE);
            mChoice2.setText("Play Again?");
            mChoice2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    finish();
                }
            });
        }
        else
        {
            mChoice1.setText(mCurrentPage.getChoice1().getText());
            mChoice2.setText(mCurrentPage.getChoice2().getText());

            mChoice1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int nextPage = mCurrentPage.getChoice1().getNextPage();
                    loadPage(nextPage);
                }
            });

            mChoice2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int nextPage = mCurrentPage.getChoice2().getNextPage();
                    loadPage(nextPage);
                }
            });
        }
    }
}
