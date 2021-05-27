# cloud-oauth
get token
curl --request GET \
  --url 'http://client:123@127.0.0.1:9888/auth/oauth/token?username=admin&password=123456&grant_type=password&scope=read' \
  --cookie JSESSIONID=5493A035C5979D57CA7968BC2E6250F8
  
check token
  curl --request POST \
  --url 'http://client:123@127.0.0.1:9888/auth/oauth/check_token?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdE9hdXRoIl0sInVzZXJfbmFtZSI6ImFkbWluIiwiZXhwIjoxNjIyMDk4Mjc3LCJ1c2VyTmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOm51bGwsImp0aSI6InBOb3lMamZNRTRqaDU4MXBuSGdsTWktV1RoWSIsImNsaWVudF9pZCI6ImNsaWVudCJ9.7z1Gqmz9J8r8REAcHOXs-ZtokOJtDQ9XZ8-8RlqZf30' \
  --cookie JSESSIONID=5493A035C5979D57CA7968BC2E6250F8
  
gateway test to yahoo
  curl --request GET \
  --url http://127.0.0.1:9888/test \
  --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsidGVzdE9hdXRoIl0sInVzZXJfbmFtZSI6ImFkbWluIiwiZXhwIjoxNjIyMDk4Mjc3LCJ1c2VyTmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOm51bGwsImp0aSI6InBOb3lMamZNRTRqaDU4MXBuSGdsTWktV1RoWSIsImNsaWVudF9pZCI6ImNsaWVudCJ9.7z1Gqmz9J8r8REAcHOXs-ZtokOJtDQ9XZ8-8RlqZf30'
