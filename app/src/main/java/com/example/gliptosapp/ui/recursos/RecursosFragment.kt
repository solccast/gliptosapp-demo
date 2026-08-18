package com.example.gliptosapp.ui.recursos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.fragment.findNavController
import com.example.gliptosapp.databinding.FragmentRecursosBinding
import com.example.gliptosapp.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecursosFragment: BaseFragment() {
    private var _binding: FragmentRecursosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecursosBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnDescargar.setOnClickListener {
            openUrl(RecursosLinks.LIBRO_CAMINANDO)
        }

        binding.btnVer.setOnClickListener {
            openUrl(RecursosLinks.DOCUMENTAL_CAMINANDO)
        }
        binding.btnVerCaminandoTierrasNuevas.setOnClickListener {
            openUrl(RecursosLinks.LIBRO_TIERRAS_NUEVAS)
        }

        // Redes sociales
        binding.btnInstagram.setOnClickListener {
            openUrl(RecursosLinks.INSTAGRAM)
        }
        binding.btnFacebook.setOnClickListener {
            openUrl(RecursosLinks.FACEBOOK)
        }
        binding.btnYoutube.setOnClickListener {
            openUrl(RecursosLinks.YOUTUBE)
        }
        binding.btnWeb.setOnClickListener {
            openUrl(RecursosLinks.WEB)
        }

    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}