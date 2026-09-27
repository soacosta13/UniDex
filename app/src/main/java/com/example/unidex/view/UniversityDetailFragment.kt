package com.example.unidex.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unidex.R
import com.example.unidex.databinding.FragmentUniversityDetailBinding
import com.example.unidex.model.University

//displays full details of a university. receives the data through fragments arguments
class UniversityDetailFragment : Fragment() {
    private var _binding: FragmentUniversityDetailBinding? = null
    private val binding get() = _binding!!

    companion object{
        private const val ARG_UNIVERSITY = "arg_university"

        //create fragments with arguments
        fun newInstance(university: University): UniversityDetailFragment {
            return UniversityDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_UNIVERSITY,university)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUniversityDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val university = arguments?.getParcelable<University>(ARG_UNIVERSITY) ?: return

        bindUniversity(university)

        binding.btnBack.setOnClickListener {
            requireActivity().finish() //closes detailactivity and returns to main
        }
    }

    private fun bindUniversity(university: University) {
        binding.tvName.text = university.name

        val location = listOfNotNull(university.stateProvince, university.country)
            .joinToString(", ")
        binding.tvLocation.text = location

        binding.tvCountryCode.text = getString(
            R.string.label_country_code,
            university.countryCode ?: getString(R.string.country_code_not_available)
        )

        binding.tvDomains.text = university.domains.joinToString (", ")

        val websiteUrl = university.webPages.firstOrNull()
        binding.btnVisitWebsite.isEnabled = websiteUrl != null
        binding.btnVisitWebsite.setOnClickListener {
            websiteUrl.let {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null //prevents memory leak
    }
}