package com.alitech.beoneseye;

import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText firstName, emailId, lastName, location,
			password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private FirebaseAuth mAuth;
	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		mAuth = FirebaseAuth.getInstance();
		initViews();
		setListeners();
		return view;
	}



	// Initialize all views
	private void initViews() {
		firstName = (EditText) view.findViewById(R.id.firstName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		lastName = (EditText) view.findViewById(R.id.lastName);
		location = (EditText) view.findViewById(R.id.location);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

		// Setting text selector over textviews
		@SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

			// Call checkValidation method
			checkValidation();
			break;

		case R.id.already_user:

			// Replace login fragment
			new LoginSignUpPage().replaceLoginFragment();
			break;
		}

	}

	// Check Validation Method
	private void checkValidation() {

		// Get all edittext texts
		String getfirstName = firstName.getText().toString();

		String getEmailId = emailId.getText().toString();
		String getlastName = lastName.getText().toString();
		String getLocation = location.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getfirstName.equals("") || getfirstName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getlastName.equals("") || getlastName.length() == 0
				|| getLocation.equals("") || getLocation.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0)

			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");

		// Check if email id valid or not
		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");

		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword))
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");

		// Else do signup or do your stuff
		else{
			mAuth.createUserWithEmailAndPassword(getEmailId, getPassword)
					.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
						@Override
						public void onComplete(@NonNull Task<AuthResult> task) {
							if (task.isSuccessful()) {
								FirebaseUser user = mAuth.getCurrentUser();
								FirebaseDatabase database = FirebaseDatabase.getInstance();
								DatabaseReference myTable = database.getReference("Users");
								DatabaseReference myRow=myTable.child(user.getUid());
								myRow.child("First Name").setValue(getfirstName);
								myRow.child("Last Name").setValue(getlastName);
								myRow.child("Email").setValue(getEmailId);
								myRow.child("Location").setValue(getLocation);
								Toast.makeText(getActivity(), "Your Have successfully Signed Up", Toast.LENGTH_SHORT).show();
								startActivity(new Intent(getActivity(),LoginSignUpPage.class));
							} else {
								new CustomToast().Show_Toast(getActivity(), view,
										"Sign Up Failed Try Again Later");

							}
						}
					});
		}

	}
}
