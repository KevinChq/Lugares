package com.lugares.ui.lugar

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lugares.R
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
        binding.etWeb.setText(args.lugar.web)

        binding.tvLatitud.text = args.lugar.longitud.toString()
        binding.tvLatitud.text = args.lugar.latitud.toString()
        binding.tvAltura.text = args.lugar.altura.toString()

        binding.btAgregar.setOnClickListener { UpdateLugar() }
        binding.btAgregar.setOnClickListener { modifcarLugar() }
        binding.btAgregar.setOnClickListener { escribirCorreo() }
        binding.btPhone.setOnClickListener { llamarLugar() }
        binding.btWhatsapp.setOnClickListener { enviarWhatsApp() }
        binding.btWeb.setOnClickListener { verWeb() }
        binding.btLocation.setOnClickListener { verMapa() }

        setHasOptionsMenu(true)
        return binding.root

    }

    private fun escribirCorreo() {
        val para = binding.etEmail.text.toString()
        if (para.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "mensage/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(para))
            intent.putExtra(Intent.EXTRA_SUBJECT,
            getString(R.string.msg_saludos)+" "+binding.etNombre.text)
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.msg_mensaje_correo))
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG).show()
        }
    }

    private fun verMapa() {
        val latitud = binding.tvLatitud.text.toString().toDouble()
        val longitud = binding.tvLongitud.text.toString().toDouble()
        if (latitud.isFinite() && longitud.isFinite()) {
            val location = Uri.parse("geo:$latitud,$longitud?z=18")
            val intent = Intent(Intent.ACTION_VIEW, location)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG).show()
        }
    }

    private fun verWeb() {
        val sitio = binding.etTelefono.text.toString()
        if (sitio.isNotEmpty()) {
            val uri = Uri.parse("http://$sitio")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG).show()
        }
    }

    private fun enviarWhatsApp() {
        val telefono = binding.etTelefono.text.toString()
        if (telefono.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = "whatsapp://send?phone=506$telefono&text="+
                    getString(R.string.msg_saludos)
            intent.setPackage("com.whatsapp")
            intent.data = Uri.parse(uri)
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG).show()
        }
    }

    private fun llamarLugar() {
        val telefono = binding.etTelefono.text.toString()
        if (telefono.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$telefono")
            if (requireActivity().checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requireActivity()
                    .requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 105)
            } else {
                requireActivity().startActivity(intent)
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.msg_datos), Toast.LENGTH_LONG).show()
        }
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
                val lugar = Lugar(0, nombre, correo, telefono, web, 0.0,0.0,0.0,"","")
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