package uni.mobile.mobileapp.rest.atv.view;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uni.mobile.mobileapp.R;
import uni.mobile.mobileapp.rest.Book;
import uni.mobile.mobileapp.rest.MyHolder;
import uni.mobile.mobileapp.rest.RestLocalMethods;
import uni.mobile.mobileapp.rest.Shelf;
import uni.mobile.mobileapp.rest.atv.holder.SimpleViewHolder;
import uni.mobile.mobileapp.rest.atv.model.TreeNode;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Bogdan Melnychuk on 2/10/15.
 */
public class AndroidTreeView {
    private static final String NODES_PATH_SEPARATOR = ";";
    private static final int CAMERA_PERMISSION_CODE = 100;

    protected TreeNode mRoot;
    private Context mContext;
    private boolean applyForRoot;
    private int containerStyle = 0;
    private Class<? extends TreeNode.BaseNodeViewHolder> defaultViewHolderClass = SimpleViewHolder.class;
    private TreeNode.TreeNodeClickListener nodeClickListener;
    private TreeNode.TreeNodeLongClickListener nodeLongClickListener;
    private boolean mSelectionModeEnabled;
    private boolean mUseDefaultAnimation = true;
    private boolean use2dScroll = false;
    private boolean enableAutoToggle = true;

    private Button captureImageButton, loadImageButton, detectTextButton, copyTextButton;
    private ImageView imageView;
    private EditText textView;
    private ImageButton rotateLeftButton, rotateRightButton;
    private LinearLayout rotateButtons;

    private Bitmap imageBitmap;
    private Uri photoURI;
    private String currentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;
    private static int RESULT_LOAD_IMAGE = 2;

    private ClipboardManager myClipboard;
    private ClipData myClip;


    public AndroidTreeView(Context context) {
        mContext = context;
    }

    public void setRoot(TreeNode mRoot) {
        this.mRoot = mRoot;
    }

    public AndroidTreeView(Context context, TreeNode root) {
        mRoot = root;
        mContext = context;
    }

    public void setDefaultAnimation(boolean defaultAnimation) {
        this.mUseDefaultAnimation = defaultAnimation;
    }

    public void setDefaultContainerStyle(int style) {
        setDefaultContainerStyle(style, false);
    }

    public void setDefaultContainerStyle(int style, boolean applyForRoot) {
        containerStyle = style;
        this.applyForRoot = applyForRoot;
    }

    public void setUse2dScroll(boolean use2dScroll) {
        this.use2dScroll = use2dScroll;
    }

    public boolean is2dScrollEnabled() {
        return use2dScroll;
    }

    public void setUseAutoToggle(boolean enableAutoToggle) {
        this.enableAutoToggle = enableAutoToggle;
    }

    public boolean isAutoToggleEnabled() {
        return enableAutoToggle;
    }

    public void setDefaultViewHolder(Class<? extends TreeNode.BaseNodeViewHolder> viewHolder) {
        defaultViewHolderClass = viewHolder;
    }

    public void setDefaultNodeClickListener(TreeNode.TreeNodeClickListener listener) {
        nodeClickListener = listener;
    }

    public void setDefaultNodeLongClickListener(TreeNode.TreeNodeLongClickListener listener) {
        nodeLongClickListener = listener;
    }

    public void expandAll() {
        expandNode(mRoot, true);
    }

    public void collapseAll() {
        for (TreeNode n : mRoot.getChildren()) {
            collapseNode(n, true);
        }
    }


    public View getView(int style) {
        final ViewGroup view;
        if (style > 0) {
            ContextThemeWrapper newContext = new ContextThemeWrapper(mContext, style);
            view = use2dScroll ? new TwoDScrollView(newContext) : new ScrollView(newContext);
        } else {
            view = use2dScroll ? new TwoDScrollView(mContext) : new ScrollView(mContext);
        }

        Context containerContext = mContext;
        if (containerStyle != 0 && applyForRoot) {
            containerContext = new ContextThemeWrapper(mContext, containerStyle);
        }
        final LinearLayout viewTreeItems = new LinearLayout(containerContext, null, containerStyle);

        viewTreeItems.setId(R.id.tree_items);
        viewTreeItems.setOrientation(LinearLayout.VERTICAL);
        view.addView(viewTreeItems);

        mRoot.setViewHolder(new TreeNode.BaseNodeViewHolder(mContext) {
            @Override
            public View createNodeView(TreeNode node, Object value) {
                return null;
            }

            @Override
            public ViewGroup getNodeItemsView() {
                return viewTreeItems;
            }
        });

        expandNode(mRoot, false);
        return view;
    }

    public View getView() {
        return getView(-1);
    }


    public void expandLevel(int level) {
        for (TreeNode n : mRoot.getChildren()) {
            expandLevel(n, level);
        }
    }

    private void expandLevel(TreeNode node, int level) {
        if (node.getLevel() <= level) {
            expandNode(node, false);
        }
        for (TreeNode n : node.getChildren()) {
            expandLevel(n, level);
        }
    }

    public void expandNode(TreeNode node) {
        expandNode(node, false);
    }

    public void collapseNode(TreeNode node) {
        collapseNode(node, false);
    }

    public String getSaveState() {
        final StringBuilder builder = new StringBuilder();
        getSaveState(mRoot, builder);
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    public void restoreState(String saveState) {
        if (!TextUtils.isEmpty(saveState)) {
            collapseAll();
            final String[] openNodesArray = saveState.split(NODES_PATH_SEPARATOR);
            final Set<String> openNodes = new HashSet<>(Arrays.asList(openNodesArray));
            restoreNodeState(mRoot, openNodes);
        }
    }

    private void restoreNodeState(TreeNode node, Set<String> openNodes) {
        for (TreeNode n : node.getChildren()) {
            if (openNodes.contains(n.getPath())) {
                expandNode(n);
                restoreNodeState(n, openNodes);
            }
        }
    }

    private void getSaveState(TreeNode root, StringBuilder sBuilder) {
        for (TreeNode node : root.getChildren()) {
            if (node.isExpanded()) {
                sBuilder.append(node.getPath());
                sBuilder.append(NODES_PATH_SEPARATOR);
                getSaveState(node, sBuilder);
            }
        }
    }

    public void toggleNode(TreeNode node) {
        if (node.isExpanded()) {
            collapseNode(node, false);
        } else {
            expandNode(node, false);
        }

    }

    private void collapseNode(TreeNode node, final boolean includeSubnodes) {
        node.setExpanded(false);
        TreeNode.BaseNodeViewHolder nodeViewHolder = getViewHolderForNode(node);

        if (mUseDefaultAnimation) {
            collapse(nodeViewHolder.getNodeItemsView());
        } else {
            nodeViewHolder.getNodeItemsView().setVisibility(View.GONE);
        }
        nodeViewHolder.toggle(false);
        if (includeSubnodes) {
            for (TreeNode n : node.getChildren()) {
                collapseNode(n, includeSubnodes);
            }
        }
    }

    private void expandNode(final TreeNode node, boolean includeSubnodes) {
        node.setExpanded(true);
        final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(node);
        parentViewHolder.getNodeItemsView().removeAllViews();


        parentViewHolder.toggle(true);

        for (final TreeNode n : node.getChildren()) {
            addNode(parentViewHolder.getNodeItemsView(), n);

            if (n.isExpanded() || includeSubnodes) {
                expandNode(n, includeSubnodes);
            }

        }
        if (mUseDefaultAnimation) {
            expand(parentViewHolder.getNodeItemsView());
        } else {
            parentViewHolder.getNodeItemsView().setVisibility(View.VISIBLE);
        }

    }

    private void addNode(ViewGroup container, final TreeNode n) {
        final TreeNode.BaseNodeViewHolder viewHolder = getViewHolderForNode(n);
        final View nodeView = viewHolder.getView();
        container.addView(nodeView);
        if (mSelectionModeEnabled) {
            viewHolder.toggleSelectionMode(mSelectionModeEnabled);
        }

        nodeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n.getClickListener() != null) {
                    n.getClickListener().onClick(n, n.getValue());
                } else if (nodeClickListener != null) {
                    nodeClickListener.onClick(n, n.getValue());
                }
                if (enableAutoToggle) {
                    toggleNode(n);
                }
                if( ((MyHolder.IconTreeItem) n.getValue()).icon == R.drawable.ic_shelficon ){
                    Shelf clickedShelf = (Shelf) n.getRestValue();
                    if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)  == PackageManager.PERMISSION_DENIED) {
                        // Requesting the permission
                        ActivityCompat.requestPermissions((Activity)mContext, new String[] { Manifest.permission.CAMERA }, CAMERA_PERMISSION_CODE);
                    }
                    else {
                        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.layout_custom_text_recognition, null);
                        captureImageButton = alertLayout.findViewById(R.id.capture_image);
                        loadImageButton = alertLayout.findViewById(R.id.load_image);
                        detectTextButton = alertLayout.findViewById(R.id.detect_text_image);
                        copyTextButton = alertLayout.findViewById(R.id.copy_text);
                        imageView = alertLayout.findViewById(R.id.image_view);
                        textView = alertLayout.findViewById(R.id.text_display);
                        rotateLeftButton = alertLayout.findViewById(R.id.rotate_left);
                        rotateRightButton = alertLayout.findViewById(R.id.rotate_right);
                        rotateButtons = alertLayout.findViewById(R.id.rotate_buttons);

                        captureImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                textView.setText("");
                                copyTextButton.setVisibility(View.GONE);
                                dispatchTakePictureIntent();
                            }
                        });

                        loadImageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                copyTextButton.setVisibility(View.GONE);
                                textView.setText("");
                                loadImageFromGallery();
                            }
                        });

                        detectTextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (imageBitmap != null) {
                                    detectTextFromImage();
                                }
                                else Toast.makeText(mContext, "Image missing. Load an image or take a photo!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        copyTextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                myClipboard = (ClipboardManager) mContext.getSystemService(CLIPBOARD_SERVICE);
                                String text;
                                text = textView.getText().toString();

                                myClip = ClipData.newPlainText("text", text);
                                myClipboard.setPrimaryClip(myClip);

                                Toast.makeText(mContext, "Text Copied", Toast.LENGTH_SHORT).show();
                            }
                        });

                        rotateLeftButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageBitmap = rotateImage(imageBitmap, -90);
                                imageView.setImageBitmap(imageBitmap);
                            }
                        });

                        rotateRightButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageBitmap = rotateImage(imageBitmap, 90);
                                imageView.setImageBitmap(imageBitmap);
                            }
                        });
                        RestLocalMethods.setContext(mContext);
                        new AlertDialog.Builder(mContext)
                                .setTitle("Add books to " + clickedShelf.getName())
                                .setMessage("You can add several books by scanning them with camera or from your onw personal image!\n Insert one title per row.")
                                .setView(alertLayout) // this is set the view from XML inside AlertDialog
                                //.setCancelable(false) // disallow cancel of AlertDialog on click of back button and outside touch
                                .setPositiveButton("Create Books", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String allTitles = textView.getText().toString();
                                        for(String title: allTitles.trim().split("\n")) RestLocalMethods.createBook(RestLocalMethods.getMyUserId(), clickedShelf.getHouseId(), clickedShelf.getRoomId(), clickedShelf.getWallId(), clickedShelf.getId(), new Book(title, "", "", clickedShelf.getId(), clickedShelf.getWallId(), clickedShelf.getRoomId(), clickedShelf.getHouseId()), null);
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .show();
                    }
                }
            }
        });

        nodeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (n.getLongClickListener() != null) {
                    return n.getLongClickListener().onLongClick(n, n.getValue());
                } else if (nodeLongClickListener != null) {
                    return nodeLongClickListener.onLongClick(n, n.getValue());
                }
                if (enableAutoToggle) {
                    toggleNode(n);
                }
                return false;
            }
        });
    }

    //------------------------------------------------------------
    //  Selection methods

    public void setSelectionModeEnabled(boolean selectionModeEnabled) {
        if (!selectionModeEnabled) {
            // TODO fix double iteration over tree
            deselectAll();
        }
        mSelectionModeEnabled = selectionModeEnabled;

        for (TreeNode node : mRoot.getChildren()) {
            toggleSelectionMode(node, selectionModeEnabled);
        }

    }

    public <E> List<E> getSelectedValues(Class<E> clazz) {
        List<E> result = new ArrayList<>();
        List<TreeNode> selected = getSelected();
        for (TreeNode n : selected) {
            Object value = n.getValue();
            if (value != null && value.getClass().equals(clazz)) {
                result.add((E) value);
            }
        }
        return result;
    }

    public boolean isSelectionModeEnabled() {
        return mSelectionModeEnabled;
    }

    private void toggleSelectionMode(TreeNode parent, boolean mSelectionModeEnabled) {
        toogleSelectionForNode(parent, mSelectionModeEnabled);
        if (parent.isExpanded()) {
            for (TreeNode node : parent.getChildren()) {
                toggleSelectionMode(node, mSelectionModeEnabled);
            }
        }
    }

    public List<TreeNode> getSelected() {
        if (mSelectionModeEnabled) {
            return getSelected(mRoot);
        } else {
            return new ArrayList<>();
        }
    }

    // TODO Do we need to go through whole tree? Save references or consider collapsed nodes as not selected
    private List<TreeNode> getSelected(TreeNode parent) {
        List<TreeNode> result = new ArrayList<>();
        for (TreeNode n : parent.getChildren()) {
            if (n.isSelected()) {
                result.add(n);
            }
            result.addAll(getSelected(n));
        }
        return result;
    }

    public void selectAll(boolean skipCollapsed) {
        makeAllSelection(true, skipCollapsed);
    }

    public void deselectAll() {
        makeAllSelection(false, false);
    }

    private void makeAllSelection(boolean selected, boolean skipCollapsed) {
        if (mSelectionModeEnabled) {
            for (TreeNode node : mRoot.getChildren()) {
                selectNode(node, selected, skipCollapsed);
            }
        }
    }

    public void selectNode(TreeNode node, boolean selected) {
        if (mSelectionModeEnabled) {
            node.setSelected(selected);
            toogleSelectionForNode(node, true);
        }
    }

    private void selectNode(TreeNode parent, boolean selected, boolean skipCollapsed) {
        parent.setSelected(selected);
        toogleSelectionForNode(parent, true);
        boolean toContinue = skipCollapsed ? parent.isExpanded() : true;
        if (toContinue) {
            for (TreeNode node : parent.getChildren()) {
                selectNode(node, selected, skipCollapsed);
            }
        }
    }

    private void toogleSelectionForNode(TreeNode node, boolean makeSelectable) {
        TreeNode.BaseNodeViewHolder holder = getViewHolderForNode(node);
        if (holder.isInitialized()) {
            getViewHolderForNode(node).toggleSelectionMode(makeSelectable);
        }
    }

    private TreeNode.BaseNodeViewHolder getViewHolderForNode(TreeNode node) {
        TreeNode.BaseNodeViewHolder viewHolder = node.getViewHolder();
        if (viewHolder == null) {
            try {
                final Object object = defaultViewHolderClass.getConstructor(Context.class).newInstance(mContext);
                viewHolder = (TreeNode.BaseNodeViewHolder) object;
                node.setViewHolder(viewHolder);
            } catch (Exception e) {
                throw new RuntimeException("Could not instantiate class " + defaultViewHolderClass);
            }
        }
        if (viewHolder.getContainerStyle() <= 0) {
            viewHolder.setContainerStyle(containerStyle);
        }
        if (viewHolder.getTreeView() == null) {
            viewHolder.setTreeViev(this);
        }
        return viewHolder;
    }

    private static void expand(final View v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    //-----------------------------------------------------------------
    //Add / Remove

    public void addNode(TreeNode parent, final TreeNode nodeToAdd) {
        parent.addChild(nodeToAdd);
        if (parent.isExpanded()) {
            final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
            addNode(parentViewHolder.getNodeItemsView(), nodeToAdd);
        }
    }

    public void removeNode(TreeNode node) {
        if (node.getParent() != null) {
            TreeNode parent = node.getParent();
            int index = parent.deleteChild(node);
            if (parent.isExpanded() && index >= 0) {
                final TreeNode.BaseNodeViewHolder parentViewHolder = getViewHolderForNode(parent);
                parentViewHolder.getNodeItemsView().removeViewAt(index);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(mContext, "uni.mobile.mobileapp.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                ((Activity) mContext).startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void detectTextFromImage() {
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        displayTextFromImage(firebaseVisionText);
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Error: ", e.getMessage());
                            }
                        });
    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if (blockList.size() == 0) Toast.makeText(mContext, "No text found in image.", Toast.LENGTH_SHORT).show();
        else {
            for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks()) {
                String text = block.getText();
                textView.setText(text);
            }
            copyTextButton.setVisibility(View.VISIBLE);
        }
    }

    private void loadImageFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) mContext).startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Toast.makeText(mContext, "Image saved", Toast.LENGTH_SHORT).show();
            imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), photoURI);
                imageBitmap = rotateImage(imageBitmap, 90);
                imageView.setImageBitmap(imageBitmap);
                rotateButtons.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();

            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            imageBitmap = BitmapFactory.decodeFile(picturePath);
            imageView.setImageBitmap(imageBitmap);
            rotateButtons.setVisibility(View.VISIBLE);

        }
    }
}
