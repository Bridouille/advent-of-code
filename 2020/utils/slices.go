package utils

import "golang.org/x/exp/constraints"

func Filter[Item interface{}](slice []Item, predicate func(Item) bool) []Item {
	ret := make([]Item, 0)
	for _, str := range slice {
		if predicate(str) {
			ret = append(ret, str)
		}
	}
	return ret
}

func Map[Input interface{}, Output interface{}](slice []Input, conv func(Input) Output) []Output {
	ret := make([]Output, len(slice))
	for idx, item := range slice {
		value := conv(item)
		ret[idx] = value
	}
	return ret
}

func Count[Item interface{}](slice []Item, predicate func(Item) bool) int {
	count := 0
	for _, item := range slice {
		if predicate(item) {
			count++
		}
	}
	return count
}

func Contains[Item interface{ comparable }](slice []Item, needle Item) bool {
	for _, item := range slice {
		if item == needle {
			return true
		}
	}
	return false
}

func Min[Item constraints.Ordered](slice []Item) Item {
	min := slice[0]
	for i := 1; i < len(slice); i++ {
		if slice[i] < min {
			min = slice[i]
		}
	}
	return min
}

func Max[Item constraints.Ordered](slice []Item) Item {
	max := slice[0]
	for i := 1; i < len(slice); i++ {
		if slice[i] > max {
			max = slice[i]
		}
	}
	return max
}
