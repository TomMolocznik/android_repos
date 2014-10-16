package tommolocznik.com.axisusage;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tommolocznik on 10/8/14.
 */
public class UsageListFragment extends ListFragment {

    private static final String TAG = "UsageListFragment";
    public Date currentDate;
    public Button addUsageButton;
    private ArrayList<Usage> mUsages;
    private boolean mSubtitleVisible;
    private Button eMailUsageButton;
    private Usage mUsage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mSubtitleVisible = false;
        getActivity().setTitle(R.string.app_name);
        mUsages = UsageCreate.get(getActivity()).getUsage();
        UsageAdapter adapter = new UsageAdapter(mUsages);
        setListAdapter(adapter);

    }

    @TargetApi(11)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_empty_list, container, false);
        ListView view = (ListView) v.findViewById(android.R.id.list);
        view.setEmptyView(v.findViewById(android.R.id.empty));
        getActivity().setTitle(R.string.app_name);

        addUsageButton = (Button) v.findViewById(R.id.emptyTextViewButton);
        addUsageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usage usage = new Usage();
                UsageCreate.get(getActivity()).addUsage(usage);
                Intent i = new Intent(getActivity(), UsagePagerActivity.class);
                i.putExtra(UsageFragment.EXTRA_USAGE_ID, usage.getId());
                startActivityForResult(i, 0);
            }
        });

//        ListView listView = (ListView)v.findViewById(android.R.id.list);
//        registerForContextMenu(listView);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            registerForContextMenu(listView);
//        } else   {
//                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                getActivity().getActionBar().setSubtitle(R.string.new_usage);
            }
        }

        ListView listView = (ListView) v.findViewById(android.R.id.list);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.usage_list_item_context, menu);
                    return true;
                }

                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                }

                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_usage:
                            UsageAdapter adapter = (UsageAdapter) getListAdapter();
                            UsageCreate usageCreate = UsageCreate.get(getActivity());
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    usageCreate.deleteUsage(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        ((UsageAdapter) getListAdapter())
                .notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_usage_list, menu);
        getActivity().setTitle(R.string.app_name);

        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);

        if (mSubtitleVisible && showSubtitle != null) {
            getActivity().setTitle(R.string.new_usage);
        }

    }

    @TargetApi(11)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_usage:

                Usage usage = new Usage();
                UsageCreate.get(getActivity()).addUsage(usage);
                Intent i = new Intent(getActivity(), UsagePagerActivity.class);
                i.putExtra(UsageFragment.EXTRA_USAGE_ID, usage.getId());
                startActivityForResult(i, 0);
                return true;

            case R.id.menu_item_show_subtitle:
                if (getActivity().getActionBar().getSubtitle() == null) {
                    getActivity().getActionBar().setSubtitle(R.string.new_usage);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                } else {
                    getActivity().getActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.usage_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        UsageAdapter adapter = (UsageAdapter) getListAdapter();
        Usage usage = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_usage:
                UsageCreate.get(getActivity()).deleteUsage(usage);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        Usage c = ((UsageAdapter) getListAdapter()).getItem(position);
        // Log.d(TAG, c.getTitle() + " was clicked");

        Intent i = new Intent(getActivity(), UsagePagerActivity.class);
        i.putExtra(UsageFragment.EXTRA_USAGE_ID, c.getId());
        startActivityForResult(i, 0);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ((UsageAdapter) getListAdapter()).notifyDataSetChanged();
    }

    private class UsageAdapter extends ArrayAdapter<Usage> {
        public UsageAdapter(ArrayList<Usage> crimes) {
            super(getActivity(), 0, crimes);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // if we weren't given a view, inflate one
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item_usage, null);
            }


            Usage c = getItem(position);


            TextView itemTextView = (TextView) convertView.findViewById(R.id.usage_list_item_itemTextView);
            itemTextView.setText("  Item ID - " + c.getItem());

            TextView itemLotView = (TextView) convertView.findViewById(R.id.usage_list_lot_itemTextView);
            itemLotView.setText("  Lot - " + c.getLot());

            TextView dateTextView = (TextView) convertView.findViewById(R.id.usage_list_item_dateTextView);
            currentDate = c.getDate();
            java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE MMM dd yyyy HH:mm");
            String formattedCurrentDate = simpleDateFormat.format(currentDate);
            dateTextView.setText("  Date Usage Added -   " + formattedCurrentDate);

            CheckBox addedCheckBox = (CheckBox) convertView.findViewById(R.id.usage_list_item_addedCheckBox);
            addedCheckBox.setChecked(c.isAdded());

            return convertView;
        }
    }

}