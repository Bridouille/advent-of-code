package utils

import "strconv"

func Filter(slice []string, predicate func(string) bool) []string {
	ret := make([]string, 0)
	for _, str := range slice {
		if predicate(str) {
			ret = append(ret, str)
		}
	}
	return ret
}

func ToIntSlice(slice []string) []int {
	ret := make([]int, len(slice))
	for idx, str := range slice {
		value, err := strconv.Atoi(str)
		if err != nil {
			panic(err)
		}
		ret[idx] = value
	}
	return ret
}
