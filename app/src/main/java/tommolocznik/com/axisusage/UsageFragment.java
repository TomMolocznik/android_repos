package tommolocznik.com.axisusage;

import java.util.Date;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * Created by tommolocznik on 10/8/14.
 */
public class UsageFragment extends Fragment

{
    public static final String EXTRA_USAGE_ID = "axis_usage.USAGE_ID";
    public static final String DIALOG_DATE = "date";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_PHOTO = 1;



    private Usage mUsage;
    private EditText mItemField;
    private EditText mLotField;
    private Button mDateButton;
    private CheckBox mAddCheckBox;
    public Date currentDate;
    ImageButton mPhotoButton;
    ImageView mPhotoView;

    public static UsageFragment newInstance( UUID usageId)
    {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_USAGE_ID,usageId);
        UsageFragment fragment = new UsageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.new_usage);
        UUID usageId = (UUID)getArguments().getSerializable( EXTRA_USAGE_ID);
        mUsage = UsageCreate.get(getActivity()).getUsage(usageId);

    }

    @Override public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        { case android.R.id.home:
            if (NavUtils.getParentActivityName(getActivity())!=null)
            {
                NavUtils.navigateUpFromSameTask(getActivity());
            }
            return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
    @TargetApi(11)
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)

    {
        View v = inflater.inflate(R.layout.fragment_usage, parent, false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            if (NavUtils.getParentActivityName(getActivity()) != null)
            {
                getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        mItemField = (EditText)v.findViewById(R.id.usage_item);
        mItemField.setText(mUsage.getItem());
        mItemField.addTextChangedListener(new TextWatcher()

        {
            public void onTextChanged(CharSequence c, int start, int before, int count)
            {
                mUsage.setItem(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after)
            {
                // This space intentionally left blank
            }

            public void afterTextChanged( Editable c)
            {

            }
        });

        mLotField = (EditText)v.findViewById(R.id.usage_lot);
        mLotField.setText(mUsage.getLot());
        mLotField.addTextChangedListener(new TextWatcher()

        {
            public void onTextChanged(CharSequence c, int start, int before, int count)
            {
                mUsage.setLot(c.toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after)
            {

            }

            public void afterTextChanged( Editable c)
            {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.usage_date);
        updateDate();


        mDateButton.setOnClickListener( new View.OnClickListener()
        { public void onClick( View v)
            {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mUsage.getDate());
                dialog.setTargetFragment(UsageFragment.this, REQUEST_DATE);
                dialog.show( fm, DIALOG_DATE);
            }
        });

        mPhotoButton = (ImageButton)v.findViewById(R.id.usage_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // launch the camera activity
                Intent i = new Intent(getActivity(), UsageCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mAddCheckBox = (CheckBox)v.findViewById(R.id.usage_added);
        mAddCheckBox.setChecked(mUsage.isAdded());
        mAddCheckBox.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged( CompoundButton buttonView, boolean isChecked)
            {
                // Set the crime's solved property
                mUsage.setAdded(isChecked);
            }
        });

        // if camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            mPhotoButton.setEnabled(false);
        }

        mPhotoView = (ImageView)v.findViewById(R.id.usage_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Photo p = mUsage.getPhoto();
                if (p == null)
                    return;

                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                String path = getActivity()
                        .getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.createInstance(path)
                        .show(fm, DIALOG_IMAGE);
            }
        });

        return v;
    }

    private void showPhoto() {
        // (re)set the image button's image based on our photo
        Photo p = mUsage.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity()
                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }

    private void updateDate()
    {
        currentDate = mUsage.getDate();
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("EEE MMM dd yyyy hh mm");
        String formattedCurrentDate = simpleDateFormat.format(currentDate);
        mDateButton.setText(formattedCurrentDate);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mUsage.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO) {
            // create a new Photo object and attach it to the crime
            String filename = data
                    .getStringExtra(UsageCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo p = new Photo(filename);
                mUsage.setPhoto(p);
                showPhoto();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        UsageCreate.get(getActivity()).saveUsages();
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        PictureUtils.cleanImageView( mPhotoView);
    }




}
