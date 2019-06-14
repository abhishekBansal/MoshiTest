package com.example.moshitest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RawRes
import android.util.Log
import com.squareup.moshi.*
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import java.lang.reflect.Type
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val moshi = Moshi.Builder()
            // ... add your own JsonAdapters and factories ...
            .add(
                PolymorphicJsonAdapterFactory.of(Vehicle::class.java, "type")
                    .withSubtype(Car::class.java, "car")
                    .withSubtype(Truck::class.java, "truck")
            )
            .add(SkipBadElementsListAdapter.Factory)
            .build()

        val string = getFileContent(R.raw.test_object)
        val adapter: JsonAdapter<List<Vehicle>> =
            moshi.adapter(Types.newParameterizedType(List::class.java, Vehicle::class.java))

        string?.let {
            val vehicles: List<Vehicle>? = adapter.fromJson(string)
            Log.d("test", "success")
        }
    }

    private fun getFileContent(@RawRes id: Int): String? {
        return try {
            val inputStream = resources.openRawResource(id)
            val b = ByteArray(inputStream.available())
            inputStream.read(b)
            String(b)
        } catch (e: Exception) {
            // e.printStackTrace();
            null
        }
    }

    class SkipBadElementsListAdapter(private val elementAdapter: JsonAdapter<Any?>) :
        JsonAdapter<List<Any?>>() {
        object Factory : JsonAdapter.Factory {
            override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
                if (annotations.isNotEmpty() || Types.getRawType(type) != List::class.java) {
                    return null
                }
                val elementType = Types.collectionElementType(type, List::class.java)
                val elementAdapter = moshi.adapter<Any?>(elementType)
                return SkipBadElementsListAdapter(elementAdapter)
            }
        }

        override fun fromJson(reader: JsonReader): List<Any?>? {
            val result = mutableListOf<Any?>()
            reader.beginArray()
            while (reader.hasNext()) {
                try {
                    val peeked = reader.peekJson()
                    result += elementAdapter.fromJson(peeked)
                } catch (ignored: JsonDataException) {
                }
                reader.skipValue()
            }
            reader.endArray()
            return result

        }

        override fun toJson(writer: JsonWriter, value: List<Any?>?) {
            if (value == null) {
                throw NullPointerException("value was null! Wrap in .nullSafe() to write nullable values.")
            }
            writer.beginArray()
            for (i in value.indices) {
                elementAdapter.toJson(writer, value[i])
            }
            writer.endArray()
        }
    }
}
