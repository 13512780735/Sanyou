package com.likeits.sanyou.message.ui;

import android.os.Bundle;

import com.likeits.sanyou.R;
import com.likeits.sanyou.message.fragment.ContactListFragment;


public class FriendActivity extends ChatBaseActivity {

    private ContactListFragment contactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        contactListFragment = new ContactListFragment();
        contactListFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, contactListFragment).commit();
    }
}
