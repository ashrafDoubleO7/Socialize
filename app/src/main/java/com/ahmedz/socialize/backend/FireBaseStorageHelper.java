package com.ahmedz.socialize.backend;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import durdinapps.rxfirebase2.RxFirebaseStorage;
import io.reactivex.Completable;
import io.reactivex.Maybe;

import static com.ahmedz.socialize.utils.Util.getCurrentTime;
import static com.ahmedz.socialize.utils.Util.isValid;



@SuppressWarnings("VisibleForTests")
public class FireBaseStorageHelper {
	private static final String AVATAR_FILE = "avatar";
	private static final String CHAT = "chat";
	private static final String POST = "post";
	private static FireBaseStorageHelper instance;
	private static StorageReference storageRef;

	public synchronized static FireBaseStorageHelper getInst() {
		if (instance == null)
			instance = new FireBaseStorageHelper();
		return instance;
	}

	private FireBaseStorageHelper() {
		storageRef = FirebaseStorage.getInstance().getReference();
	}

	public Maybe<String> putFile(String userEmail, Uri imagePath) {
		// optional imagePath
		if (!isValid(imagePath))
			return Maybe.empty();

		StorageReference avatarRef = storageRef.child(userEmail).child(AVATAR_FILE);
		return RxFirebaseStorage.putFile(avatarRef, imagePath)
				.map((taskSnapshot) -> taskSnapshot.getDownloadUrl().toString());
	}

	public Maybe<String> putGroupImage(String groupUID, Uri uri) {
		StorageReference chatImageRef = storageRef.child(groupUID).child(getCurrentTime()+"");
		return RxFirebaseStorage.putFile(chatImageRef, uri)
				.map((taskSnapshot) -> taskSnapshot.getDownloadUrl().toString());
	}

	public Completable deleteImage(String groupUID, String fileName) {
		StorageReference chatImageRef = storageRef.child(groupUID).child(fileName);
		return RxFirebaseStorage.delete(chatImageRef);
	}
}
