package kz.vozon.vozon.ui.info


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import kotlinx.android.synthetic.main.fragment_location_extra.view.*
import kz.vozon.vozon.R
import kz.vozon.vozon.utility.get
import kz.vozon.vozon.utility.snack


class LocationExtraFragment : Fragment() {

    var v: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.enter_address)
        v =  inflater.inflate(R.layout.fragment_location_extra, container, false)
        return v
    }

    private fun done(){
        try{
            (activity as kz.vozon.vozon.ui.info.LocationActivity).i.
                    putExtra(kz.vozon.vozon.ui.info.LocationActivity.EXTRA,v!!.v_extra.get("extra is empty"))
            (activity as kz.vozon.vozon.ui.info.LocationActivity).next(0)

        }catch (ex: IllegalStateException){
            activity!!.snack(ex.message ?: getString(R.string.something_went_wrong))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_done, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                done()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
