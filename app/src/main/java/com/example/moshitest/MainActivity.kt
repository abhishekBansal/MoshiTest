package com.example.moshitest

import android.os.Bundle
import android.support.annotation.RawRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.squareup.moshi.*
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

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
            .add(SkipBadElementsMapAdapter.Factory)
            .build()

//        val string = getFileContent(R.raw.test_map)


//        string?.let {
        val map = mutableMapOf<VehicleType2, Int>()
        map[VehicleType2.TYPE1] = 1
        map[VehicleType2.TYPE2] = 2
        val adapter: JsonAdapter<Map<VehicleType2, Int>> =
            moshi.adapter(Types.newParameterizedType(Map::class.java, VehicleType2::class.java, Integer::class.java))

        Log.d("test", "success")
        Log.i("test", adapter.toJson(map))
//        }
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

        // Factory creates a adapter and then cache it for given type
        // an adapter is for a specific type, a factory is called for every
        // type and gets to choose whether to return an adapter or ignore it (return null).
        object Factory : JsonAdapter.Factory {
            override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
                val rawType = Types.getRawType(type)
                if (annotations.isNotEmpty() || rawType != List::class.java) {
                    return null
                }

                if (type !is ParameterizedType) {
                    return null // No type parameter? This factory doesn't apply.
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

    private class SkipBadElementsMapAdapter(
        private val elementKeyAdapter: JsonAdapter<Any?>,
        private val elementValueAdapter: JsonAdapter<Any?>
    ) : JsonAdapter<Map<Any?, Any?>>() {
        // Factory creates a adapter and then cache it for given type
        // an adapter is for a specific type, a factory is called for every
        // type and gets to choose whether to return an adapter or ignore it (return null).
        object Factory : JsonAdapter.Factory {
            override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
                val rawType = Types.getRawType(type)
                if (annotations.isNotEmpty() || rawType != Map::class.java) {
                    return null
                }

                if (type !is ParameterizedType) {
                    return null // No type parameter? This factory doesn't apply.
                }

                val types = type.actualTypeArguments
                val elementKeyAdapter = moshi.nextAdapter<Any?>(this, types[0], annotations)
                val elementValueAdapter = moshi.nextAdapter<Any?>(this, types[1], annotations)

                return SkipBadElementsMapAdapter(elementKeyAdapter, elementValueAdapter)
            }
        }


        override fun fromJson(reader: JsonReader): Map<Any?, Any?>? {
            val result = mutableMapOf<Any?, Any?>()
            reader.beginObject()
            while (reader.hasNext()) {
                try {

                    val peekedKey = reader.peekJson()
                    val key = elementKeyAdapter.fromJsonValue(peekedKey.nextName())
                    reader.skipValue()

                    val peekedValue = reader.peekJson()
                    val value = elementValueAdapter.fromJsonValue(peekedValue.readJsonValue())
                    Log.d("", value.toString())
                    result[key] = value
                } catch (ignored: JsonDataException) {
                    ignored.printStackTrace()
                    Log.w("Moshi", "Ignoring Map Element: ${ignored.message}")
                }
                reader.skipValue()
            }
            reader.endObject()
            return result

        }

        override fun toJson(writer: JsonWriter, value: Map<Any?, Any?>?) {
            if (value == null) {
                writer.nullValue()
            } else {
                writer.beginObject()
                value.forEach {
                    writer.name(elementKeyAdapter.toJsonValue(it.key).toString())
                        .value(elementValueAdapter.toJson(it.value))
                }
                writer.endObject()
            }
        }
    }
}
