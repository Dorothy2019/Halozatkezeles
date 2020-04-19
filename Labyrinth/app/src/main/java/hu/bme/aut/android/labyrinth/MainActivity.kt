package hu.bme.aut.android.labyrinth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hu.bme.aut.android.labyrinth.event.MoveUserResponseEvent
import hu.bme.aut.android.labyrinth.event.WriteMessageResponseEvent
import hu.bme.aut.android.labyrinth.network.LabyrinthAPI
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MOVE_LEFT = 1
        private const val MOVE_UP = 2
        private const val MOVE_RIGHT = 3
        private const val MOVE_DOWN = 4
    }

    private lateinit var labyrinthAPI: LabyrinthAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isNetworkAvailable = Runtime.getRuntime().exec("ping -c 1 google.com").waitFor()

        if(isNetworkAvailable != 0){
            Toast.makeText(applicationContext,"Turn on Internet",Toast.LENGTH_LONG).show()
        }


            labyrinthAPI = LabyrinthAPI()

            btnDown.setOnClickListener {
                async {
                    val response = labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_DOWN)

                    val duration = measureTimeMillis {
                        labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_DOWN)
                    }
                    EventBus.getDefault().post(MoveUserResponseEvent(response, duration.toInt()))
                }
            }

            btnUp.setOnClickListener {
                async {
                    val response = labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_UP)

                    val duration = measureTimeMillis {
                        labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_UP)
                    }
                    EventBus.getDefault().post(MoveUserResponseEvent(response, (duration).toInt()))
                }
            }

            btnLeft.setOnClickListener {
                async {
                    val response = labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_LEFT)

                    val duration = measureTimeMillis {
                        labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_LEFT)
                    }
                    EventBus.getDefault().post(MoveUserResponseEvent(response, (duration).toInt()))
                }
            }

            btnRight.setOnClickListener {
                async {
                    val response = labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_RIGHT)

                    val duration = measureTimeMillis {
                        labyrinthAPI.moveUser(etUsername.text.toString(), MOVE_RIGHT)
                    }
                    EventBus.getDefault().post(MoveUserResponseEvent(response, (duration).toInt()))
                }
            }

            btnSend.setOnClickListener {
                async {
                    val response = labyrinthAPI.writeMessage(
                        etUsername.text.toString(),
                        etMessage.text.toString()
                    )
                    EventBus.getDefault().post(WriteMessageResponseEvent(response, 0))
                }
            }
        
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMoveUserResponse(event: MoveUserResponseEvent) {
        showResponse("Move User Response: ${event.response}", "Time Spent: ${event.time}")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWriteMessageResponse(event: WriteMessageResponseEvent) {
        showResponse("Write Message Response: ${event.response}", "Time Spent: ${event.time}")
    }

    private fun async(call: () -> Unit) = Thread { call() }.start()


    private fun showResponse(response: String, time: String?) {
        tvResponse.text = response
        tvTime.text = time
    }

}
