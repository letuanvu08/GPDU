import { useEffect, useRef, useState } from "react";
import { useSelector } from "react-redux";

export default function useMap() {
  const { locationSelected } = useSelector(state => state.routing);
  const initViewport = {
    latitude: 10.7758439,
    longitude: 106.70175527777778,
    zoom: 13,
  };
  const [viewport, setViewport] = useState(initViewport);
  useEffect(() => {
    console.log("locationSelected", locationSelected);
    setViewport({
      ...viewport,
      latitude: locationSelected.latitude,
      longitude: locationSelected.longitude,
      zoom: initViewport.zoom,
    });
  }, [locationSelected]);
  return { viewport, setViewport };
}