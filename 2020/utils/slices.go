package utils

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
