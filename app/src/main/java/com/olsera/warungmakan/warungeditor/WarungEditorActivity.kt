package com.olsera.warungmakan.warungeditor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.olsera.warungmakan.R
import com.olsera.warungmakan.databinding.ActivityWarungEditorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.location.LocationManager
import android.content.pm.PackageManager
import android.location.Address
import android.util.Log
import android.widget.Toast

import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import com.adevinta.leku.*
import com.adevinta.leku.locale.SearchZoneRect
import com.google.android.gms.maps.model.Marker
import com.olsera.repository.model.Warung
import com.thedeanda.lorem.LoremIpsum
import kotlin.random.Random


class WarungEditorActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel by viewModel<WarungEditorViewModel>()

    private var idWarung = 0L
    private var editMode = EditMode.CREATE_NEW

    private lateinit var binding: ActivityWarungEditorBinding
    private lateinit var map: GoogleMap
    private var marker: Marker? = null

    private val permission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWarungEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(KEY_ID_WARUNG)) {
            idWarung = intent.getLongExtra(KEY_ID_WARUNG, -1L)
            editMode = EditMode.UPDATE_EXISTING
            viewModel.getWarungDetail(idWarung)
            binding.textTitle.text = "Update Warung"
        } else {
            binding.buttonDelete.isGone = true
        }

        setupView()

        if (!hasPermissions(this, permission)) {
            ActivityCompat.requestPermissions(this, permission, REQ_CODE_PERMISSION)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_EDIT_MAP && data != null) {
            val latitude = data.getDoubleExtra(LATITUDE, 0.0)
            val longitude = data.getDoubleExtra(LONGITUDE, 0.0)
            marker?.remove()

            val latLng = LatLng(latitude, longitude)
            val markerOptions = MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .draggable(false)
                .visible(true)
            marker = map.addMarker(markerOptions)
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_CODE_PERMISSION) {
            if (hasPermissions(this, permission)) {
                setupMapPositionToCurrentLocation()
            } else {
                Toast.makeText(
                    this,
                    "Aplikasi tidak dapat bejalan dengan baik tanpa perizinan lokasi",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupView() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        setupObserver()

        binding.buttonEditMap.setOnClickListener {
            showMapPicker()
        }
        binding.buttonDelete.setOnClickListener {
            viewModel.deleteWarung(viewModel.warungDetail.value?.id ?: -1)
        }
        binding.buttonCancel.setOnClickListener {
            finish()
        }
        binding.buttonSave.setOnClickListener {
            if (editMode == EditMode.CREATE_NEW) {
                saveWarung()
            } else {
                updateWarung()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        map.uiSettings.setAllGesturesEnabled(false)
        if (editMode == EditMode.UPDATE_EXISTING) {
            if (viewModel.warungDetail.value != null) {
                setupGoogleMapPosition()
            }
        } else {
            setupMapPositionToCurrentLocation()
        }
    }

    private fun setupObserver() {
        viewModel.warungDetail.observe(this, {
            binding.inputName.setText(it.name)
            binding.inputAddress.setText(it.address)
            binding.inputCity.setText(it.city)
            binding.inputZipCode.setText(it.zipCode)
            if (this::map.isInitialized) {
                setupGoogleMapPosition()
            }
            if (it.isActive) {
                binding.buttonActive.isChecked = true
            } else {
                binding.buttonInactive.isChecked = true
            }
        })

        viewModel.warungIsSaved.observe(this, {
            Toast.makeText(this, "Warung telah disimpan", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            finish()
        })

        viewModel.warungIsDeleted.observe(this, {
            Toast.makeText(this, "Warung telah dihapus", Toast.LENGTH_LONG).show()
            setResult(RESULT_OK)
            finish()
        })
    }

    private fun saveWarung() {
        val warung = Warung(
            0,
            binding.inputName.getText(),
            "https://picsum.photos/id/${Random.nextInt(500)}/200/300",
            binding.inputAddress.getText(),
            binding.inputCity.getText(),
            binding.inputZipCode.getText(),
            marker?.position?.longitude ?: 0.0,
            marker?.position?.latitude ?: 0.0,
            LoremIpsum.getInstance().getParagraphs(1, 2),
            binding.buttonActive.isChecked
        )
        viewModel.saveNewWarung(warung)
    }

    private fun updateWarung() {
        val warung = viewModel.warungDetail.value!!
        val warungNew = Warung(
            warung.id,
            binding.inputName.getText(),
            warung.imageUrl,
            binding.inputAddress.getText(),
            binding.inputCity.getText(),
            binding.inputZipCode.getText(),
            marker?.position?.longitude ?: 0.0,
            marker?.position?.latitude ?: 0.0,
            warung.desc,
            binding.buttonActive.isChecked
        )
        viewModel.updateWarung(warungNew)
    }

    private fun setupGoogleMapPosition() {
        val latLng = LatLng(
            viewModel.warungDetail.value!!.latitude,
            viewModel.warungDetail.value!!.longitude
        )
        val markerOptions = MarkerOptions()
            .position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker())
            .draggable(false)
            .visible(true)
        marker = map.addMarker(markerOptions)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
    }

    @SuppressLint("MissingPermission")
    private fun setupMapPositionToCurrentLocation() {
        if (hasPermissions(this, permission)) {
            val lm = getSystemService(LOCATION_SERVICE) as LocationManager
            val lastLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lastLoc?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .draggable(false)
                    .visible(true)
                marker = map.addMarker(markerOptions)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
            }
        }
    }

    private fun showMapPicker() {
        val locationPickerIntent = LocationPickerActivity.Builder()
            .withDefaultLocaleSearchZone()
            .withStreetHidden()
            .withCityHidden()
            .withZipCodeHidden()
            .withSatelliteViewHidden()
            .withGoogleTimeZoneEnabled()
            .withVoiceSearchHidden()
            .withUnnamedRoadHidden()
            .build(applicationContext)

        startActivityForResult(locationPickerIntent, REQ_CODE_EDIT_MAP)
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    enum class EditMode {
        CREATE_NEW, UPDATE_EXISTING
    }

    companion object {
        private const val KEY_ID_WARUNG = "ID_WARUNG"
        private const val REQ_CODE_PERMISSION = 566
        private const val REQ_CODE_EDIT_MAP = 15

        /**
         * Intent for creating new Warung
         */
        fun createIntent(context: Context): Intent {
            return Intent(context, WarungEditorActivity::class.java)
        }

        /**
         * Intent for updating existing Warung.
         * pass [com.olsera.repository.model.Warung.id] as [idWarung]
         */
        fun createIntent(context: Context, idWarung: Long): Intent {
            val intent = Intent(context, WarungEditorActivity::class.java)
            intent.putExtra(KEY_ID_WARUNG, idWarung)
            return intent
        }
    }
}