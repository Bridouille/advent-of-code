package utils

func Filter(slice []string, predicate func(string) bool) []string {
	ret := make([]string, 0)
	for _, str := range slice {
		if predicate(str) {
			ret = append(ret, str)
		}
	}
	return ret
}
