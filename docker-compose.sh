#!/usr/bin/env bash
#
# Copyright TNO Geologische Dienst Nederland
#
# Alle rechten voorbehouden.
# Niets uit deze software mag worden vermenigvuldigd en/of openbaar gemaakt door middel van druk, fotokopie,
# microfilm of op welke andere wijze dan ook, zonder voorafgaande toestemming van TNO.
#
# Indien deze software in opdracht werd uitgebracht, wordt voor de rechten en verplichtingen van opdrachtgever
# en opdrachtnemer verwezen naar de Algemene Voorwaarden voor opdrachten aan TNO, dan wel de betreffende
# terzake tussen de partijen gesloten overeenkomst.
#

len=$#
if [ $len -ne 1 ]; then
  echo "Missing arguments: at least one argument needed ./docker-compose.sh <profiles> e.g. ./docker-compose.sh gmn,gmw,gld"
  echo "  Possible arguments: frd, gar, gld, gmn, gmw, all or comma-separated multiple profiles"
  exit
fi

profile_definitions=(
  "frd"
  "gar"
  "gld"
  "gmn"
  "gmw"
  )

IFS=',' read -ra profiles <<< "$1"
profileString=""
for profile in "${profiles[@]}"
do

  if [ $profile == "all" ]; then

    echo "Found profile all. Starting up all containers"
    for profile in "${profile_definitions[@]}"
    do
      profileString="${profileString} --profile ${profile}"
    done
    break;

  else

    regexValue="\<${profile}\>"
    if [[ ${profile_definitions[@]} =~ ${regexValue} ]]
    then
      profileString="${profileString} --profile ${profile}"
    else
      echo "ERROR: Profile '${profile}' does not exist. Valid profiles: [${profile_definitions[@]}]"
      exit
    fi
  fi

done

echo "Executing command: docker compose ${profileString} up --build"
docker compose ${profileString} up --build