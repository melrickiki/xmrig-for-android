package com.xmrigforandroid.utils

import android.os.Build
import java.io.IOException
import java.io.RandomAccessFile


class CPUTools {

    private var sCPUTempSysFile = ""

    private val CPU_TEMP_SYS_FILE = arrayOf(
            "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
            "/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
            "/sys/devices/virtual/thermal/thermal_zone1/temp",
            "/sys/devices/virtual/thermal/thermal_zone0/temp",
            "/sys/devices/virtual/hwmon/hwmon1/temp1_input",
            "/sys/devices/platform/omap/omap_temp_sensor.0/temperature",
            "/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature",
            "/sys/devices/platform/tegra_tmon/temp1_input",
            "/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/ext_temperature",
            "/sys/devices/platform/tegra-tsensor/tsensor_temperature",
            "/sys/devices/platform/s5p-tmu/temperature",
            "/sys/devices/platform/s5p-tmu/curr_temp",
            "/sys/devices/virtual/thermal/thermal_zone9/temp",
            "/sys/class/thermal/thermal_zone0/temp",
            "/sys/class/thermal/thermal_zone1/temp",
            "/sys/class/thermal/thermal_zone3/temp",
            "/sys/class/thermal/thermal_zone4/temp",
            "/sys/class/i2c-adapter/i2c-4/4-004c/temperature",
            "/sys/class/hwmon/hwmon0/temp1_input",
            "/sys/class/hwmon/hwmonX/temp1_input",
            "/sys/class/hwmon/hwmon0/device/temp1_input",
            "/sys/kernel/debug/tegra_thermal/temp_tj",
            "/sys/htc/cpu_temp"
    )

    fun getCurrentCPUTemperature(): Float {
        if (sCPUTempSysFile.isEmpty()) return getCPUTempSysFile()

        // No CPU temperature sensor
        return if (sCPUTempSysFile.equals("err")) 0.0f else getCPUTempFromFile(sCPUTempSysFile)
    }

    fun getCPUTempFromFile(sFile: String?): Float {
        var output = 0.0f
        val reader: RandomAccessFile
        val line: String
        try {
            reader = RandomAccessFile(sFile, "r")
            line = reader.readLine()
            if (line != null) {
                output = line.toFloat()
                if (output > 1000.0f && Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    output /= 1000.0f
                }
                if (output > 100.0f) { // error while reading file
                    sCPUTempSysFile = ""
                    return 0.0f
                }
            }
        } catch (e: IOException) {
            sCPUTempSysFile = ""
            e.printStackTrace()
            return 0.0f
        }
        return output
    }

    fun getCPUTempSysFile(): Float {
        var output: Float
        for (sysFile in CPU_TEMP_SYS_FILE) {
            output = getCPUTempFromFile(sysFile)
            if (output > 0.0f && output < 100.0f) { // ugly temporary workaround
                sCPUTempSysFile = sysFile
                return output
            }
        }
        sCPUTempSysFile = "err"
        return 0.0f
    }

}