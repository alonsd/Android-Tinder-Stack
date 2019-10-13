package com.etiennelawlor.tinderstack.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.etiennelawlor.tinderstack.R;

public class ApproveDialogFragment extends DialogFragment implements View.OnClickListener {

  private String mSelectedLawyerName;

  private Button mDismissButton;
  private TextView mSelectedLawyerTextView;


  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_fragment_approve_background);
    View view = inflater.inflate(R.layout.dialog_fragment_approve, container, false);
    view.setClipToOutline(true);
    mDismissButton = view.findViewById(R.id.dialog_fragment_approve_dismiss);
    mSelectedLawyerTextView = view.findViewById(R.id.dialog_fragment_approve_lawyer_name);
    mDismissButton.setOnClickListener(this);

    setLawyerName();

    return view;
  }

  private void setLawyerName(){
    if (mSelectedLawyerName != null) {
      mSelectedLawyerTextView.setText(mSelectedLawyerName);
    }
  }


  @Override
  public void onClick(View view) {
    dismiss();
  }

  public void setApprovedLawyer(String lawyerName) {
    this.mSelectedLawyerName = lawyerName;
  }

}
