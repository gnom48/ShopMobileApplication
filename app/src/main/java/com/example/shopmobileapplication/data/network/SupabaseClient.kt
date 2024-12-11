package com.example.shopmobileapplication.data.network

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.GoTrue
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClient {
    val supabaseBaseUrl = "https://vqsawhpgqxwnkyryaytd.supabase.co"

    val client = createSupabaseClient(
//        "http://31.129.102.158:8000/",
//        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyAgCiAgICAicm9sZSI6ICJzZXJ2aWNlX3JvbGUiLAogICAgImlzcyI6ICJzdXBhYmFzZS1kZW1vIiwKICAgICJpYXQiOiAxNjQxNzY5MjAwLAogICAgImV4cCI6IDE3OTk1MzU2MDAKfQ.DaYlNEoUrrEn2Ig7tqibS-PHK5vgusbcbo7X36XVt4Q"

        supabaseUrl = supabaseBaseUrl, //"https://vqsawhpgqxwnkyryaytd.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InZxc2F3aHBncXh3bmt5cnlheXRkIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzMyNDQwMDMsImV4cCI6MjA0ODgyMDAwM30.SevMgKGkM6CzfB8e0_qf5KLko9sVPysPaJ9132en_ig"

    ) {
        install(GoTrue)
        install(Storage)
        install(Postgrest)
        install(Realtime)
    }
}