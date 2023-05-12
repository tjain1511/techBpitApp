package com.indianapp.techbpit.ApiController;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TavclanService {
    @Headers({
            "Content-Type: application/json",
            "source: android",
            "authorization-mode: AWSCognito",
            "authorization: Bearer eyJraWQiOiJicGpZNkZtekNKYnExQldIdW9KQUJpV3R0ZGZ4bENSVUxcL1Y5Slk1dzlFMD0iLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJiMzk1YWNjYy03MjZiLTQ0NTAtODQxZS02YTMwNTBlODVkYTAiLCJpc3MiOiJodHRwczpcL1wvY29nbml0by1pZHAuYXAtc291dGgtMS5hbWF6b25hd3MuY29tXC9hcC1zb3V0aC0xX0xDNlY3WHh3ZCIsImNsaWVudF9pZCI6IjJpaG5tbTdya245bjNjaWZpYTVrMjBsYXU5Iiwib3JpZ2luX2p0aSI6Ijk5ODIyM2VmLTI1ZGYtNDNkNi05ZTI2LWQ1YjNkZmU0OGFkOCIsImV2ZW50X2lkIjoiYzAwNTAwMmUtYjhkNS00YWZiLWJlYjctMzU1NGYxMmY1ZjFhIiwidG9rZW5fdXNlIjoiYWNjZXNzIiwic2NvcGUiOiJhd3MuY29nbml0by5zaWduaW4udXNlci5hZG1pbiIsImF1dGhfdGltZSI6MTY4MjQwNzA2MSwiZXhwIjoxNjgzNDgxMzE3LCJpYXQiOjE2ODMzOTQ5MTcsImp0aSI6ImMwMWQ3ZjM1LTU4YzUtNDYyZC05ZjkwLTg3NmU3NmRkMmI4ZSIsInVzZXJuYW1lIjoiYjM5NWFjY2MtNzI2Yi00NDUwLTg0MWUtNmEzMDUwZTg1ZGEwIn0.rxwvmABs3t-_CXKgjKSEAx2cbV_-kJLaJj3tkg-0XP6LWFIyP0PZ5uuhnlfP0mVEQVKPvmjME46mt9J3Kv6pqB-o7lUoMiy1EhpGJsszzXltPsybUk2_9kDE-cL8JrZKuCJUdD9porBvA0cmYSlR8XH8WSY5laAI220DpyQENDxdzvLfSaESSt9XuOmTnHwxu_Sg_uJ-j4CbZIJIxd-iC3P1THlnL5jvhKPscDiHRLY_O2KtdvTZoPgl5jlk97l4VWjL7MF7h-ZwC-RNPUXJendB2cGxNlANgvtsOX2OvcEsamNR1d1l3juSt8_o4rrEi1lKk6yn_kgUjpOafjCDXA"
    })
    @GET("/api/v2/airports/search")
    Call<AirportsSearchResponse> getAirportSearchResults(@Query("searchString") String searchString);
}
