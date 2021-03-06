package com.example.freeentproject.ui.fragments
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.freeentproject.databinding.FragmentPlayRadioBinding
import com.example.freeentproject.domain.models.ModeloRadio
import com.example.freeentproject.utils.Utils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import dagger.hilt.android.AndroidEntryPoint

/*
Fragment en el que reproducimos la radio seleccionada de la bd. Blindeamos la vista a través del
viewBinding. Creamos el objeto viewModels y el objeto navArgs() para pasarle el objeto ModeloRadio
a través del nav_grav (layout). A través de los métodos override como son onStart(), onPause(),
onStop() y onResume(). A parte creamos el método item() para convertir la url recibida a través de
un intent del fragment anterior (RadioFragment) en un MediaItem que metemos en el método
reproducir() para que el objeto ExoPlayer reproduzca la película. El método releasePlayer() nos
permite que la música se corte cuando navegamos hacia otro fragment de la aplicación.
 */

@AndroidEntryPoint
class PlayRadioFragment: Fragment(), Player.Listener {

    private var _binding: FragmentPlayRadioBinding? = null
    private val binding get() = _binding!!
    private lateinit var radio : ModeloRadio
    private lateinit var player: ExoPlayer
    private val args: PlayRadioFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayRadioBinding.inflate(inflater, container, false)
        radio = args.radio
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Utils.loadImage(radio.imagen ?: " ", binding.imgPlayRadio)
        back()
    }

    override fun onStart() {
        super.onStart()
        reproducir()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player.stop()
        player.release()
    }

    private fun reproducir() {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.videoView.player = player

        val mediaItem = item(radio.url!!)
        player.addMediaItem(mediaItem)
        player.prepare()
        player.play()
        player.isCurrentMediaItemLive
    }

    private fun item(url: String): MediaItem {
        return MediaItem.fromUri(Uri.parse(url))
    }

    private fun back(){
        binding.back.setOnClickListener {
            val direcccion = PlayRadioFragmentDirections.actionPlayRadioFragmentToRadioFragment()
            findNavController().navigate(direcccion)
        }
    }

}