
package `in`.instea.instea

import android.app.Application
import `in`.instea.instea.di.AppContainer
import `in`.instea.instea.di.AppDataContainer

class InsteaApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
