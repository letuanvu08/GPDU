export function hideApartString(str, maxLen) {
  if (str && str.length > 30) {
    return str.substring(0, maxLen) + "....";
  }
  return str;
}

export default {
  hideApartString,
};