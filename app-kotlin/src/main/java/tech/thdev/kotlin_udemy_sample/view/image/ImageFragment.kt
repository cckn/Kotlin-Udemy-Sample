package tech.thdev.kotlin_udemy_sample.view.image

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_image_sample.*
import tech.thdev.kotlin_udemy_sample.R
import tech.thdev.kotlin_udemy_sample.constant.Constant
import tech.thdev.kotlin_udemy_sample.data.RecentPhotoItem
import tech.thdev.kotlin_udemy_sample.data.model.PhotoDataSource
import tech.thdev.kotlin_udemy_sample.view.detail.DetailActivity
import tech.thdev.kotlin_udemy_sample.view.image.adapter.ImageAdapter
import tech.thdev.kotlin_udemy_sample.view.image.presenter.ImageContract
import tech.thdev.kotlin_udemy_sample.view.image.presenter.ImagePresenter

/**
 * Created by tae-hwan on 10/3/16.
 */
class ImageFragment : Fragment(), ImageContract.View {

    private val fab by lazy {
        activity.findViewById(R.id.fab) as FloatingActionButton
    }

    // Java 식의 static instance
    companion object {
        fun getInstance() = ImageFragment()
    }

    private var imageAdapter: ImageAdapter? = null

    private var presenter: ImageContract.Presenter? = null

    /**
     * ViewType 정의
     */
    private var mViewType = ImageAdapter.VIEW_TYPE_GLIDE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.fragment_image_sample, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = ImagePresenter()
        presenter?.view = this

        imageAdapter = ImageAdapter(context)

        /**
         * Model을 생성하여 셋팅한다
         */
        presenter?.photoDataSample = PhotoDataSource

        presenter?.adapterView = imageAdapter
        presenter?.adapterModel = imageAdapter

        recycler_image.adapter = imageAdapter

        fab.setOnClickListener {
            presenter?.getRecentImageSample(mViewType)
        }

        presenter?.getRecentImageSample(mViewType)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_async -> {
                changeViewType(ImageAdapter.VIEW_TYPE_ASYNC, item)
                return true
            }
            R.id.action_thread -> {
                changeViewType(ImageAdapter.VIEW_TYPE_THREAD, item)
                return true
            }
            R.id.action_glide -> {
                changeViewType(ImageAdapter.VIEW_TYPE_GLIDE, item)
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun changeViewType(viewType: Int, item: MenuItem?) {
        mViewType = ImageAdapter.VIEW_TYPE_GLIDE
        presenter?.getRecentImageSample(viewType)
        if (item?.isChecked ?: false) item?.isChecked = false
        else item?.isChecked = true
    }

    override fun showLoadFailMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        Log.e("TAG", "Exception : " + message)
    }

    override fun showLoadSuccess() {
        Toast.makeText(context, "Load success", Toast.LENGTH_SHORT).show()
    }

    override fun showLoadFail() {
        Toast.makeText(context, "Load fail", Toast.LENGTH_SHORT).show()
    }

    override fun showParcelableDetail(item: RecentPhotoItem) {
//        val intent = Intent(context, DetailParcelableActivity::class.java)
//        intent.putExtra(Constant.KEY_PHOTO_DATA, item)
//        startActivity(intent)
    }

    override fun showDetail(photoId: String) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(Constant.KEY_PHOTO_DATA, photoId)
        startActivity(intent)
    }
}