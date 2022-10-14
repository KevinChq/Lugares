package com.lugares.ui.lugar

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lugares.R
import com.lugares.databinding.ActivityMainBinding
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.model.Lugar
import com.lugares.ui.viewmodel.LugarViewModel

class AddLugarFragment : Fragment() {
    private var _binding: FragmentAddLugarBinding? = null
    private val binding get() = _binding!!

    private lateinit var lugarViewModel: LugarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddLugarBinding.inflate(inflater, container, false)

        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        binding.btAgregar.setOnClickListener { addLugar()}

        ubicaGPS()

        return binding.root
    }

    private var conPermisos: Boolean=true;
    private fun ubicaGPS() {
        val fusedLocationProviderClient : FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 105)
        }
        if (conPermisos) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                location : Location? ->
                if (location != null) {
                    binding.tvLatitud.text = "${location.latitude}"
                    binding.tvLongitud.text = "${location.longitude}"
                    binding.tvAltura.text = "${location.altitude}"
                } else {
                    binding.tvLatitud.text = getString(R.string.error)
                    binding.tvLongitud.text = getString(R.string.error)
                    binding.tvAltura.text = getString(R.string.error)
                }
            }
        }
    }

    private fun addLugar() {
        var nombre = binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) {
            val nombre = binding.etNombre.text.toString()
            val correo = binding.etEmail.text.toString()
            val telefono = binding.etTelefono.text.toString()
            val web = binding.etWeb.text.toString()
            val latitud = binding.tvLatitud.text.toString().toDouble()
            val longitud = binding.tvLongitud.text.toString().toDouble()
            val altura = binding.tvAltura.text.toString().toDouble()

            if (validos(nombre, correo, telefono, web)) {
                val lugar = Lugar(0, nombre, correo, telefono, web, latitud,longitud,altura,"","")
                lugarViewModel.addLugar(lugar)
                Toast.makeText(requireContext(),"Lugar Agregado",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(),"Faltan Datos",Toast.LENGTH_LONG).show()
            }
            findNavController().navigate(R.id.action_addLugarFragment_to_nav_lugar)

        }

    }

    private fun validos(nombre: String, correo: String, telefono: String, web: String): Boolean {
        return !(nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || web.isEmpty())
    }
}