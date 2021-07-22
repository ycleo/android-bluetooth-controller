package com.example.bluetoothcontroller

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothcontroller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT: Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BT: Int = 2

    private lateinit var binding: ActivityMainBinding
    //bluetooth adapter
    private lateinit var bAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init bluetooth adapter
        bAdapter = BluetoothAdapter.getDefaultAdapter()

        //check bluetooth is available or not
        if(bAdapter == null) {
            binding.bluetoothStatus.text = "Bluetooth is not available"
        } else {
            binding.bluetoothStatus.text = "Bluetooth is available"
        }

        //set image according to bluetooth status
        if (bAdapter.isEnabled) {
            //bluetooth is on
            binding.bluetoothImage.setImageResource(R.drawable.ic_bluetooth_on)
        } else {
            //bluetooth is off
            binding.bluetoothImage.setImageResource(R.drawable.ic_bluetooth_off)
        }

        //turn on bluetooth
        binding.turnOnButton.setOnClickListener {
            if (bAdapter.isEnabled) {
                //already enabled
                Toast.makeText(this, "Already On", Toast.LENGTH_LONG).show()
            } else {
                //turn on bluetooth
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
            }
        }

        //turn off bluetooth
        binding.turnOffButton.setOnClickListener {
            if (!bAdapter.isEnabled) {
                //already enabled
                Toast.makeText(this, "Already Off", Toast.LENGTH_LONG).show()
            } else {
                //turn on bluetooth
                bAdapter.disable()
                binding.bluetoothImage.setImageResource(R.drawable.ic_bluetooth_off)
                Toast.makeText(this, "Bluetooth turn off", Toast.LENGTH_LONG).show()
            }
        }

        //bluetooth set discoverable
        binding.discoverableButton.setOnClickListener {
            if (!bAdapter.isDiscovering) {
                Toast.makeText(this, "Making Your device discoverable", Toast.LENGTH_LONG).show()
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)
            }
        }

        //get list of paired devices
        binding.getPairedListButton.setOnClickListener {
            if (bAdapter.isEnabled) {
                binding.pairedList.text = "Paired Devices"
                val devices = bAdapter.bondedDevices
                for (device in devices) {
                    val deviceName = device.name
                    val deviceAddress = device
                    binding.pairedList.append("\nDevice: $deviceName, $device")

                }
            } else {
                Toast.makeText(this, "Please turn of bluetooth first", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ENABLE_BT ->
                if(requestCode == Activity.RESULT_OK) {
                    binding.bluetoothImage.setImageResource(R.drawable.ic_bluetooth_on)
                    Toast.makeText(this, "Bluetooth is On", Toast.LENGTH_LONG).show()
                } else {
                    //user denied to turn on bluetooth from confirmation dialog
                    Toast.makeText(this, "Could not on bluetooth", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}