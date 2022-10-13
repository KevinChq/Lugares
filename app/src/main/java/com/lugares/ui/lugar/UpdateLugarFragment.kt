package com.lugares.ui.lugar

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares.R
import com.lugares.databinding.FragmentAddLugarBinding
import com.lugares.databinding.FragmentUpdateLugarBinding
import com.lugares.model.Lugar
import com.lugares.ui.viewmodel.LugarViewModel

class UpdateLugarFragment : Fragment() {
    private var _binding: FragmentUpdateLugarBinding? = null
    private val binding get() = _binding!!

    private lateinit var lugarViewModel: LugarViewModel

    private val args by navArgs<UpdateLugarFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        _binding = FragmentUpdateLugarBinding.inflate(inflater, container, false)
        lugarViewModel = ViewModelProvider(this).get(LugarViewModel::class.java)

        binding.etNombre.setText(args.lugar.nombre)
        binding.etEmail.setText(args.lugar.correo)
        binding.etTelefono.setText(args.lugar.telefono)
        binding.etWeb.setText(args.lugar.web)

        binding.btActualizarLugar.setOnClickListener {
            modifcarLugar()
        }

        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.menu_delete) {
            deleteLugar()
        }
        return super.onContextItemSelected(item)
    }

    private fun modifcarLugar() {
        val nombre = binding.etNombre.text.toString()
        val correo = binding.etEmail.text.toString()
        val telefono = binding.etTelefono.text.toString()
        val web = binding.etWeb.text.toString()
        if (validos(nombre,correo,telefono,web)) {
            val lugar = Lugar(args.lugar.id,
            nombre,
            correo,
            telefono,
            web,
            args.lugar.longitud,
            args.lugar.latitud,
            args.lugar.altura,
            args.lugar.rutaAudio,
            args.lugar.rutaImagen)
            lugarViewModel.updateLugar(lugar)
        } else {
            Toast.makeText(requireContext(),"Faltan Datos",Toast.LENGTH_LONG).show()
        }
        findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
    }

    private fun deleteLugar() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton(getString(R.string.si))  {_,_ ->
            lugarViewModel.deleteLugar(args.lugar)
            Toast.makeText(requireContext(),
            getString(R.string.deleted)+ " ${args.lugar.nombre}!",
            Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_updateLugarFragment_to_nav_lugar)
        }
        builder.setNegativeButton(getString(R.string.no)) {_,_ ->}
        builder.setTitle(R.string.deleted)
        builder.setMessage(getString(R.string.seguroBorrar)+" ${args.lugar.nombre}?")
        builder.create().show()
    }

    private fun UpdateLugar() {
        var nombre = binding.etNombre.text.toString()
        if (nombre.isNotEmpty()) {
            val nombre = binding.etNombre.text.toString()
            val correo = binding.etEmail.text.toString()
            val telefono = binding.etTelefono.text.toString()
            val web = binding.etWeb.text.toString()
            if (validos(nombre, correo, telefono, web)) {
                val lugar = Lugar(0, nombre, correo, telefono, web, 0.0,0.0,0,"","")
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