package com.lugares.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName="lugar")
data class Lugar(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @ColumnInfo(name="nombre")
    val nombre: String,
    @ColumnInfo(name="correo")
    val correo: String?,
    @ColumnInfo(name="telefono")
    val telefono: String?,
    @ColumnInfo(name="web")
    val web: String?,
    @ColumnInfo(name="latitud")
    val latitud: String?,
    @ColumnInfo(name="longitud")
    val longitud: String?,
    @ColumnInfo(name="altura")
    val altura: String?,
    @ColumnInfo(name="rutaAudio")
    val rutaAudio: String?,
    @ColumnInfo(name="rutaImagen")
    val rutaImagen: String?
) : Parcelable
