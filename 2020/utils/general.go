package utils

import (
	"strconv"

	"golang.org/x/exp/constraints"
)

func ToInt(str string) int {
	ret, err := strconv.Atoi(str)
	if err != nil {
		panic(err)
	}
	return ret
}

func Abs[Item constraints.Signed](item Item) Item {
	if item < 0 {
		return item * -1
	}
	return item
}
